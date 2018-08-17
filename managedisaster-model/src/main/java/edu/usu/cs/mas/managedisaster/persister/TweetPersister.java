package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import sim.engine.SimState;
import edu.usu.cs.mas.managedisaster.entity.TweetEntity;

public interface TweetPersister {

  public TweetEntity save(TweetEntity tweet);
  
  public TweetEntity getTweet(Long id);
  
  public List<TweetEntity> getActiveTimedTweets();
  
  public void cleanup();
  
  public void setSimState(SimState simState);
}
