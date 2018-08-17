package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.Simulator;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import sim.engine.SimState;
import sim.engine.Steppable;
import twitter4j.Status;

public class TweetDispatcher implements Steppable {

  private static final long serialVersionUID = -6255174360787770107L;
  private static final Logger LOGGER = Logger.getLogger(TweetDispatcher.class);

  @Inject
  private TwitterHandler twitterHandler;
  @Inject
  private FirePersister firePersister;

  @Override
  public void step(SimState state){
    Simulator simulator = (Simulator) state;
    Long currentTime = (long) simulator.schedule.getTime();
    boolean isPost = postTweet(currentTime);
    if(isPost) {
      return;
    }
    FireEntity fire = readTweetAndCreateFire(currentTime);
    LOGGER.info("Saving fire time: "+currentTime+" fire:"+fire);
  }

  private boolean postTweet(Long currentTime) {
    List<Status> postedTweets = twitterHandler.threadedPostTweet(currentTime);

    if(CollectionUtils.isEmpty(postedTweets)) {
      return false;
    }
    return true;
  }

  private FireEntity readTweetAndCreateFire(Long currentTime) {
    List<Message> messages = twitterHandler.threadedReadTweet(currentTime);
    if(CollectionUtils.isEmpty(messages)) {
      return null;
    }
    FireEntity fire = null;
    for(Message message : messages) {
      fire = firePersister.generateFire(message);
    }
    return fire;
  }
}
