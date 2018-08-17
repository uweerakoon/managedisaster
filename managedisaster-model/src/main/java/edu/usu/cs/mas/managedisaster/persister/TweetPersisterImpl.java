package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.TweetEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import sim.engine.SimState;

public class TweetPersisterImpl implements TweetPersister{

  private static final Logger LOGGER = Logger.getLogger(TweetPersisterImpl.class);

  private EntityManager entityManager;
  private SimState simState;

  @Inject
  private HibernateUtil hibernateUtil;

  public TweetPersisterImpl() { }

  public TweetPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
    entityManager = hibernateUtil.getEntityManager();
  }

  @Override
  public TweetEntity save(TweetEntity tweet) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(tweet);
    hibernateUtil.commit();
    LOGGER.info("Saving the Tweet entity: "+tweet);
    return tweet;
  }

  @Override
  public TweetEntity getTweet(Long id) {
    entityManager = hibernateUtil.getEntityManager();
    TweetEntity tweet = entityManager.find(TweetEntity.class, id);
    return tweet;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<TweetEntity> getActiveTimedTweets() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select t from TweetEntity t "
        + "where t.posted = false and t.active = true "
        + "and t.tweetTime <= "+(long)simState.schedule.getTime();
    Query query = entityManager.createQuery(strQuery);
    List<TweetEntity> tweets = query.getResultList();
    return tweets;
  }

  @Override
  public void setSimState(SimState simState) {
    this.simState = simState;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select t from TweetEntity t where t.posted = true";
    Query query = entityManager.createQuery(strQuery);
    List<TweetEntity> tweetEntities = query.getResultList();
    for(TweetEntity tweetEntity : tweetEntities) {
      tweetEntity.setPosted(false);
      entityManager.persist(tweetEntity);
    }
    hibernateUtil.commit();
  }
}
