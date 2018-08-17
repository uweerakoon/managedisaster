package edu.usu.cs.mas.managedisaster.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.TweetEntity;
import edu.usu.cs.mas.managedisaster.exception.ManageDisasterServiceException;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.message.MessageType;
import edu.usu.cs.mas.managedisaster.message.Severity;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class TwitterHandlerImpl implements TwitterHandler {
  private static final Logger LOGGER = Logger.getLogger(TwitterHandlerImpl.class);
  private static final String NUMERICAL_REG = ".*\\d.*";
  private static final String NUMERICAL_DELIMETER = "[^\\d]+";

  private static final int THREAD_AWAIT_TIME = 5; // In seconds
  //https://dev.twitter.com/rest/public/rate-limits : can hit only 180 times per 15 minutes
  private static final int TIME_INTERVAL_FOR_READ = 5000; // In milliseconds

  private static boolean isPostingTweet;
  private static boolean isReadingTweet;
  private static long timeReadTweet = 0;

  @Inject
  private BuildingPersister buildingPersister; 
  @Inject
  private TweetPersister tweetPersister;

  private Twitter twitter;
  private User user;
  private long latestTweetId;
  private Date currentDate;
  private List<Status> postedTweetsTobeDeleted; // clean up the posted tweets from the twitter.com
  private List<Long> postedTweetIds; // if this is empty, then agents are reading tweets from twitter.com

  public TwitterHandlerImpl() {
    postedTweetsTobeDeleted = new ArrayList<>();
    postedTweetIds = new ArrayList<>();
    currentDate = new Date();
    latestTweetId = 0;
    twitter = new TwitterFactory().getInstance();
    try {
      user = twitter.verifyCredentials();
    }
    catch(Exception e) {
      LOGGER.fatal("Problem with creating twitter connetion: "+e);
      e.printStackTrace();
    }
  }

  public TwitterHandlerImpl(BuildingPersister buildingPersister, TweetPersister tweetPersister, 
    Twitter twitter, User user, long latestTweetId, Date currentDate) {
    this();
    this.buildingPersister = buildingPersister;
    this.tweetPersister = tweetPersister;
    this.twitter = twitter;
    this.user = user;
    this.latestTweetId = latestTweetId;
    this.currentDate = currentDate;
  }

  @Override
  public List<Message> threadedReadTweet(Long currentTime) {
    if(isReadingTweet) {
      LOGGER.info("Cannot read tweets, previous thread is still working on reading the twitters, returning back (avoid | Request Limit Exceed Exception |) ");
      return null;
    }
    if(postedTweetIds.isEmpty()) {
      LOGGER.info("Current Time: "+currentTime+" has no tweets to read because no posts");
      return null;
    }
    LOGGER.info("Current Time: "+currentTime+" is about to send a request to read tweets");
    isReadingTweet = true;
    List<Message> messages = null;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Callable<List<Message>> callable = new Callable<List<Message>> () {
      @Override
      public List<Message> call() throws Exception {
        List<Message> messages = readTweet(currentTime);
        return messages;
      }
    };
    Future<List<Message>> futureResults = executorService.submit(callable);
    executorService.shutdown();
    try {
      if(!executorService.awaitTermination(THREAD_AWAIT_TIME, TimeUnit.SECONDS)) {
        LOGGER.error("Timeout 5 seconds exceeded while waiting for read tweets");
        isReadingTweet = false;
        return null;
      }
    }
    catch(InterruptedException e) {
      LOGGER.error("Interrupt twitter read operation",e);
    }
    if(futureResults.isDone()) {
      try {
        if(futureResults.get() != null) {
          messages = futureResults.get();
          LOGGER.info("Read the message from the twitter. Messages: "+messages);
        }
      }
      catch(Exception e) {
        LOGGER.error("Problem of getting the tweets from the twitter",e);
      }
    }
    isReadingTweet = false;
    return messages;
  }

  public List<Message> readTweet(Long currentTime) {
    List<Status> tweets = null;
    try {
      if(timeReadTweet > 0 
          && (System.currentTimeMillis() - timeReadTweet) <= TIME_INTERVAL_FOR_READ) {
        return null;
      }
      tweets = twitter.getUserTimeline(user.getId());
      timeReadTweet = System.currentTimeMillis();
      LOGGER.info("Get all tweets from twitter.com. The size of the tweets: "+tweets.size());
    }
    catch(Exception e) {
      LOGGER.fatal("Problem with reading tweets: "+e);
      e.printStackTrace();
    }

    if(CollectionUtils.isEmpty(tweets)) {
      return null;
    }

    List<Status> recentTweets = 
        tweets.stream()
        .filter(t -> currentDate.before(t.getCreatedAt()) 
          && t.getId() > latestTweetId)
        .collect(Collectors.<Status>toList());

    if(CollectionUtils.isEmpty(recentTweets)) {
      return null;
    }

    List<Message> tweetMessages = populateTweetMessages(recentTweets);
    LOGGER.info("Current Time: "+currentTime+" read following recent tweets: "+recentTweets);
    for(Status recentTweet : recentTweets) {
      postedTweetIds.remove(new Long(recentTweet.getId()));
    }

    updateTweetEntities(tweetMessages, currentTime);

    return tweetMessages;
  }

  @Override
  public  List<Status> threadedPostTweet(long currentTime) {
    if(isPostingTweet) {
      LOGGER.info("Twitter Posting in progress, returning back");
      return null;
    }
    List<TweetEntity> tweets = tweetPersister.getActiveTimedTweets();
    if(CollectionUtils.isEmpty(tweets)) {
      LOGGER.info("Current Time: "+currentTime+" has no tweets to post");
      return null;
    }
    LOGGER.info("Current Time: "+currentTime+" is about to post a request to post tweets");
    isPostingTweet = true;
    List<Status> postedTweets = null;  
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Callable<List<Status>> callable = new Callable<List<Status>> () {
      @Override
      public List<Status> call() throws Exception {
        List<Status> postedTweets = postTweet(currentTime, tweets);
        return postedTweets;
      }
    };
    Future<List<Status>> futurePostedTweets = executorService.submit(callable);
    executorService.shutdown();
    try {
      if(!executorService.awaitTermination(THREAD_AWAIT_TIME, TimeUnit.SECONDS)) {
        LOGGER.error("Timeout "+THREAD_AWAIT_TIME+" seconds exceeded while waiting for read tweets");
        isPostingTweet = false;
        return null;
      }
    }
    catch(InterruptedException e) {
      LOGGER.error("Interrupt twitter read operation",e);
    }
    if(futurePostedTweets.isDone()) {
      try {
        if(futurePostedTweets.get() != null) {
          postedTweets = futurePostedTweets.get();
        }
      }
      catch(Exception e) {
        LOGGER.error("Problem of getting the tweets from the twitter",e);
      }
    }
    isPostingTweet = false;
    return postedTweets;
  }

  public List<Status> postTweet(long currentTime, List<TweetEntity> tweets) {
    List<Status> postedTweets = Lists.newArrayList();
    for(TweetEntity tweet : tweets) {
      validate(tweet);
      String tweetText = getTweetText(tweet);
      Status postedTweet = null;
      try {
        postedTweet = twitter.updateStatus(tweetText);
        LOGGER.info("Current Time: "+currentTime+" tweet: "+tweet+" Twitter: "+postedTweet);
      }
      catch(Exception e) {
        LOGGER.fatal("Cannot tweet the message: "+tweet);
        e.printStackTrace();
      }
      if(postedTweet == null) {
        continue;
      }
      if(postedTweet.getId() > 0) {
        saveTweetEntity(currentTime, tweet);
      }
      recordTweetPost(postedTweet);
      postedTweets.add(postedTweet);
    }
    return postedTweets;
  }

  private void saveTweetEntity(long currentTime, TweetEntity tweet) {
    tweet.setPosted(true);
    tweet.setPostedTime(currentTime);
    tweetPersister.save(tweet);
  }

  @Override
  public void cleanup() {
    for(Status tweet : postedTweetsTobeDeleted) {
      try {
        LOGGER.info("Deleting Tweet: "+tweet.getText());
        twitter.destroyStatus(tweet.getId());
      }
      catch(Exception e) { // continue the program without a break
        LOGGER.fatal("Cannot delete the tweet: "+tweet.getText()+" Exception: "+e);
      }
    }
  }

  private void updateTweetEntities(List<Message> tweetMessages, Long currentTime) {
    for(Message message : tweetMessages) {
      if(message.getTweetId() == null) {
        continue;
      }
      TweetEntity entity = tweetPersister.getTweet(message.getTweetId());
      entity.setReadTime(currentTime);
      tweetPersister.save(entity);
    }
  }

  private String getTweetText(TweetEntity tweet) {
    StringBuilder sb = new StringBuilder()
        .append(tweet.getId())
        .append(" ")
        .append(tweet.getSeverity().toString())
        .append(" ")
        .append(tweet.getMessageType().toString())
        .append(" at ")
        .append(tweet.getBuilding().getName());
    return sb.toString();
  }

  private void validate(TweetEntity tweet) {
    Preconditions.checkNotNull(tweet,"Tweeting message is null");
    Preconditions.checkNotNull(tweet.getMessageType(), "Message type is null");
    Preconditions.checkNotNull(tweet.getSeverity(), "Severity is null");
    Preconditions.checkNotNull(tweet.getBuilding(), "Building is null");
    if(StringUtils.isEmpty(tweet.getBuilding().getName())) {
      throw new ManageDisasterServiceException("The building has no name");
    }
  }

  private List<Message> populateTweetMessages(List<Status> recentTweets) {
    List<Message> tweetMessages = new ArrayList<>();
    for(Status recentTweet : recentTweets) {
      Message message = map(recentTweet);
      if(message == null) {
        continue;
      }
      tweetMessages.add(message);
      if(recentTweet.getId() > latestTweetId) {
        latestTweetId = recentTweet.getId();
      }
      if(currentDate.before(recentTweet.getCreatedAt())) {
        currentDate = recentTweet.getCreatedAt();
      }
    }
    return tweetMessages;
  }

  private void recordTweetPost(Status tweet) {
    postedTweetsTobeDeleted.add(tweet);
    postedTweetIds.add(tweet.getId());
  }

  private Message map(Status tweet) {
    String tweetText = tweet.getText();
    Message message = new Message();

    MessageType messageType = MessageType.getMessageType(tweetText);
    if(messageType == null) {
      return null;
    }
    message.setMessageType(messageType);

    Severity severity = Severity.getSeverity(tweetText);
    if(severity == null) {
      return null;
    }
    message.setSeverity(severity);

    BuildingEntity building = buildingPersister.getBuilding(tweetText.split("at ")[1]);
    if(building == null){
      return null;
    }
    message.setBuilding(building);

    Long tweetId = getTweetId(tweetText);
    if(tweetId != null) {
      message.setTweetId(tweetId);
    }

    return message;
  }

  public Long getTweetId(String tweetText) {
    if(!tweetText.matches(NUMERICAL_REG)) {
      return null;
    }
    Scanner scanner = new Scanner(tweetText);
    Long id = scanner.useDelimiter(NUMERICAL_DELIMETER).nextLong();
    scanner.close();
    return id;
  }

  public long getLatestTweetId() {
    return latestTweetId;
  }

  public void setLatestTweetId(long latestTweetId) {
    this.latestTweetId = latestTweetId;
  }

  public Date getCurrentDate() {
    return currentDate;
  }

  public void setCurrentDate(Date currentDate) {
    this.currentDate = currentDate;
  }
  
  public static void main(String[] args) {
    User user;
    Twitter twitter = new TwitterFactory().getInstance();
    Status postedTweet = null;
    try {
      user = twitter.verifyCredentials();
    }
    catch(Exception e) {
      LOGGER.fatal("Problem with creating twitter connetion: "+e);
      e.printStackTrace();
    };
    try {
      postedTweet = twitter.updateStatus("This is a test post:"+new Date());
      LOGGER.info("Posted Twitter: "+postedTweet);
    }
    catch(Exception e) {
      LOGGER.fatal("Cannot tweet the message: "+postedTweet);
      e.printStackTrace();
    }
  }
}
