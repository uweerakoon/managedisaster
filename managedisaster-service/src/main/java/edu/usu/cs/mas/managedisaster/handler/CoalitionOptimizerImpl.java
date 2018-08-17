package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.common.CoalitionStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;

public class CoalitionOptimizerImpl implements CoalitionOptimizer {

  private static final Logger LOGGER = Logger.getLogger(CoalitionOptimizerImpl.class);
  
  @Inject
  private CoalitionPersister coalitionPersister;
  
  @Inject
  private FireStationPersister fireStationPersister;
  
  /**
   * If all the agents in the fire station are members of 
   * a same coalition then the coalition is considered as optimized
   */
  @Override
  public void optimizeFormedCoaltion() {
    List<CoalitionEntity> coalitions = coalitionPersister.getFormingCoalitions();
    for(CoalitionEntity coalition : coalitions) {
      // get all the agents in the fire station
      FireStationEntity fireStation = fireStationPersister.getFireStation(coalition.getFireStation().getId());
      List<AgentEntity> agents = fireStation.getActiveAgents();
      boolean isAllInCoalition = false;
      // check all the members are in the coalition
      for(AgentEntity agent : agents) {
        if(!agent.isFormingCoalition(coalition.getId())) {
          isAllInCoalition = false;
          break;
        }
        if(!isAllInCoalition) {
          isAllInCoalition = true;
        }
      }
      // if yes set the coalition from forming to optimizing
      if(isAllInCoalition) {
        coalition.setStatus(CoalitionStatus.OPTIMIZING);
        coalitionPersister.save(coalition);
      }
      else { // else do not change the status of the coalition, put some logs
        LOGGER.error("Cannot make the coalition set into optimizing status, because some of the agents in the fire station are not in"
            + " the coalition");
      }
      
    }
  }
}
