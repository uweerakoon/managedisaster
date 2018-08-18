package edu.usu.cs.mas.managedisaster.service.util;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.handler.TwitterHandler;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentStatPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentUtilityPersister;
import edu.usu.cs.mas.managedisaster.persister.BurningForestStatPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionForestPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionStatPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;

public class CleanerImpl implements Cleaner {

  private static final Logger LOGGER = Logger.getLogger(CleanerImpl.class);

  @Inject
  private TwitterHandler twitterHandler;
  @Inject
  private AgentPersister agentPersister;
  @Inject
  private FirePersister firePersister;
  @Inject
  private TweetPersister tweetPersister;
  @Inject
  private FireStationPersister fireStationPersister;
  @Inject
  private AgentStatPersister agentStatPersister;
  @Inject
  private BurningForestStatPersister burningForestStatPersister;
  @Inject
  private CoalitionStatPersister coalitionStatPersister;
  @Inject
  private CoalitionPersister coalitionPersister;
  @Inject
  private CoalitionForestPersister coalitionForestPersister;
  @Inject
  private AgentUtilityPersister agentUtilityPersister;
  @Inject
  private AgentCoalitionPersister agentCoalitionPersister;

  @Override
  public void cleanup() {
    LOGGER.info("Clearning up data records in the database");
    agentPersister.cleanup();
    twitterHandler.cleanup();
    firePersister.cleanup();
    tweetPersister.cleanup();
    fireStationPersister.cleanup();
  }

  @Override
  public void initialize() {
    LOGGER.info("Clearning the Statistics Tables");
    agentStatPersister.cleanup();
    burningForestStatPersister.cleanup();
    coalitionStatPersister.cleanup();
    coalitionForestPersister.cleanup();
    agentCoalitionPersister.cleanup();
    agentUtilityPersister.cleanup();
    agentPersister.cleanup();
    coalitionPersister.cleanup();
  }
}
