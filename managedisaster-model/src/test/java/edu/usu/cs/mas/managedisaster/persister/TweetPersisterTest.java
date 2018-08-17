package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import sim.engine.Schedule;
import sim.engine.SimState;
import edu.usu.cs.mas.managedisaster.entity.TweetEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;

public class TweetPersisterTest {

  private static final Long AGENT_ID = 5L;
  private static final Long CURRENT_TIME = 200L;
  
  private HibernateUtil hibernateUtil;
  private TweetPersister tweetPersister;
  
  @Mock
  private SimState simState;
  @Mock
  private Schedule schedule;
  
  public TweetPersisterTest() {
    hibernateUtil = new HibernateUtilImpl();
    tweetPersister = new TweetPersisterImpl(hibernateUtil);
    MockitoAnnotations.initMocks(this);
    simState.schedule = schedule;
    tweetPersister.setSimState(simState);
  }
  
  /*@Test
  public void testGetTweetByAgentId() {
    Mockito.when(schedule.getSteps()).thenReturn(CURRENT_TIME);
    List<TweetEntity> tweets = tweetPersister.getTweetByAgentId(AGENT_ID);
    assertTrue(!tweets.isEmpty());
    TweetEntity tweet = tweets.get(0);
    assertEquals(AGENT_ID, tweet.getPostedAgent().getId());
  }*/
}
