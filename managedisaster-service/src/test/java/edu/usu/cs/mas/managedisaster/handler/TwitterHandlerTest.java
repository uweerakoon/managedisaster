package edu.usu.cs.mas.managedisaster.handler;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.TweetEntity;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.message.MessageType;
import edu.usu.cs.mas.managedisaster.message.Severity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterHandlerTest {
  
  private static final String WALMART = "walmart";
  private static final long CURRENT_TIME = 1L;

  private static Twitter twitter;
  private static User user;
  
  private static BuildingPersister buildingPersister; 
 
  @Mock
  private TweetPersister tweetPersister;
  
  private long latestTweetId;
  private Date currentDate;
  private TwitterHandlerImpl twitterHandler;
  ResponseList<Status> statuses;
  
  @BeforeClass
  public static void main() {
    twitter = new TwitterFactory().getInstance();
    try {
      user = twitter.verifyCredentials();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    buildingPersister = new BuildingPersisterImpl(new HibernateUtilImpl());
  }
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Ignore // Changes to account email: 4 per hour.
  // https://support.twitter.com/articles/15364-about-twitter-limits-update-api-dm-and-following
  @Test
  public void testPostAndReadTweet() throws Exception{
    Message message = new Message().withMessageType(MessageType.FIRE).withSeverity(Severity.MILD).withForest(new ForestEntity().withName(WALMART));
    twitterHandler = new TwitterHandlerImpl(buildingPersister, tweetPersister, twitter, user, latestTweetId, currentDate);
    AgentModel agentModel = new AgentModel().withId(1L);
    AgentPlayer agent = new AgentPlayer().withAgentModel(agentModel);
    TweetEntity tweet = getTweetEntity();
    List<TweetEntity> tweets = Arrays.asList(tweet);
    
    Mockito.when(tweetPersister.getActiveTimedTweets()).thenReturn(tweets);
    Mockito.when(tweetPersister.getTweet(1L)).thenReturn(tweet);
    Mockito.when(tweetPersister.save(tweet)).thenReturn(tweet);
    
    List<Status> statuses = twitterHandler.threadedPostTweet(CURRENT_TIME);
    Status status = statuses.get(0);
    
    DateTime minuteBefore = new DateTime(status.getCreatedAt()).minusMinutes(1);
    currentDate = minuteBefore.toDate();
    latestTweetId = status.getId() - 1;
    twitterHandler.setCurrentDate(currentDate);
    twitterHandler.setLatestTweetId(latestTweetId);
    
    agentModel.setId(2L);
    List<Message> messages = twitterHandler.readTweet(CURRENT_TIME);
    
    twitter.destroyStatus(status.getId());
    message = messages.get(0);
    assertEquals(WALMART, message.getForest().getName());
    assertEquals(MessageType.FIRE, message.getMessageType());
    assertEquals(Severity.MILD, message.getSeverity());
  }
  
  private TweetEntity getTweetEntity() {
    ForestEntity building = getBuildingEntity();
    TweetEntity entity = new TweetEntity()
                          .withId(1L)
                          .withMessageType(MessageType.FIRE)
                          .withSeverity(Severity.MILD)
                          .withForest(building);
    return entity;
  }
  
  private ForestEntity getBuildingEntity() {
    ForestEntity entity = new ForestEntity().withName(WALMART);
    return entity;
  }

}
