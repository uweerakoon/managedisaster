package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;

import edu.usu.cs.mas.managedisaster.message.Message;
import twitter4j.Status;

public interface TwitterHandler {

	public List<Message> threadedReadTweet(Long currentTime);
  
  public  List<Status> threadedPostTweet(long currentTime);
  
  public void cleanup();

}
