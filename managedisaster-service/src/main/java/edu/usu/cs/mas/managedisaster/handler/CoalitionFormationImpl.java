package edu.usu.cs.mas.managedisaster.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.google.common.annotations.VisibleForTesting;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.common.CoalitionForestStatus;
import edu.usu.cs.mas.managedisaster.common.CoalitionStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentCoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentUtilityEntity;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.exception.ManageDisasterServiceException;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentUtilityPersister;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import sim.engine.SimState;

public class CoalitionFormationImpl implements CoalitionFormation {

	private static final long serialVersionUID = 8230253446334831477L;
	private static final Logger LOGGER = Logger.getLogger(CoalitionFormationImpl.class);
	private static final MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());
	
	@Inject
	private CoalitionPersister coalitionPersister;
	@Inject
	private AgentPersister agentPersister;
	@Inject
	private FireStationPersister fireStationPersister;
	@Inject
	private CoalitionBuildingPersister coalitionBuildingPersister;
	@Inject
	private BuildingPersister buildingPersister;
	@Inject
	private AgentSociety agentSociety;
	@Inject
	private CoalitionOptimizer coalitionOptimizer;
	@Inject
	private CoalitionCalculator coalitionCalculator;
	@Inject
	private CoalitionCleaner coalitionCleaner;
	@Inject
	private AgentUtilityPersister AgentUtilityPersister;
	@Inject
	private AgentCoalitionPersister agentCoalitionPersister;
	@Inject
	private FirePersister firePersister;
	
	private List<CoalitionEntity> allocatedCoalitions = new ArrayList<>();
	private Set<AgentPlayer> unwantedAgentPlayers = new HashSet<>();
	
	
	@Override
  public void step(SimState state){
	  if(CollectionUtils.isEmpty(agentSociety.getCoalitionFormationAgents())) {
      return;
    }
    allocatedCoalitions.clear();
    unwantedAgentPlayers.clear();
    // need to get agents that are in the coalition formation status
    List<AgentPlayer> coalitionFormingAgents = agentSociety.getCoalitionFormationAgents();
    initiateCoalitions(coalitionFormingAgents);
    coalitionOptimizer.optimizeFormedCoaltion(); // all fire station agents in the coalition
    setFeasibleCoalitions(); // if water > fire
    coalitionCalculator.calculateUtility();
    enabledBestCoalitionToExecute();
    for(CoalitionEntity bestUtilityCoalition : allocatedCoalitions) {
      setCoalitionOptimizingToExecuting(bestUtilityCoalition);
    }
    for(AgentPlayer agentPlayer : unwantedAgentPlayers) {
      if(agentPlayer.getAgentModel().getCoalition() != null) {
        continue;
      }
      agentPlayer.getAgentModel().setCloseFires(null);
      agentPlayer.setAgentFromFCtoS();
    }
	}
	
	@Override
	public void release(AgentPlayer agentPlayer) {
	  CoalitionEntity coalitionEntity = agentPlayer.getAgentModel().getCoalition();
	  if(coalitionEntity == null) {
	    return;
	  }
	  coalitionEntity = coalitionPersister.getCoalition(agentPlayer.getAgentModel().getCoalition().getId());
	  if(coalitionEntity.getStatus() != CoalitionStatus.TERMINATE) {
	    CoalitionForestEntity coalBuildEntity = coalitionBuildingPersister.getExecutingCoalBuild(coalitionEntity);
	    shareUtility(coalitionEntity, coalBuildEntity);
	    coalitionEntity.setAgents(null);
  	  coalitionEntity.setStatus(CoalitionStatus.TERMINATE);
  	  coalitionPersister.save(coalitionEntity);
  	  if(coalBuildEntity != null && coalBuildEntity.getStatus() != CoalitionForestStatus.TERMINATE) {
  	    coalBuildEntity.setStatus(CoalitionForestStatus.TERMINATE);
  	    coalitionBuildingPersister.save(coalBuildEntity);
  	  }
  	  else {
  	    throw new ManageDisasterServiceException("Coal-build pair is missing for coal: "+coalitionEntity.getId());
  	  }
	  }
    AgentEntity agentEntity = agentPersister.getAgent(agentPlayer.getId());
	  agentPlayer.setCoalition(agentEntity, null);
	}
	
	private void shareUtility(CoalitionEntity coalitionEntity, CoalitionForestEntity coalBuildEntity) {
	  List<AgentEntity> agents = coalitionEntity.getAgents();
	  double coalUtility = coalBuildEntity.getUtility();
	  double agentUtility = coalUtility / agents.size();
	  for(AgentEntity agent : agents) {
	    AgentUtilityEntity agentUtil = new AgentUtilityEntity().withAgent(agent).withCoalition(coalitionEntity).withUtility(agentUtility);
	    AgentUtilityPersister.save(agentUtil);
	  }
	}
	
	@VisibleForTesting
	protected List<AgentPlayer> initiateCoalitions(List<AgentPlayer> coalitionFormingAgents) {
		
		for(AgentPlayer coalitionFormingAgent : coalitionFormingAgents) {
			if(coalitionFormingAgent.getAgentModel().getCoalition() != null) {
				continue;
			}
			// find a coalition that other agents of the fire station are candidates
			List<AgentCoalitionEntity> fireStationAgentCoalitions = fireStationCoalition(coalitionFormingAgent);
			if(fireStationAgentCoalitions == null) {
				// agent create the brand new coalition
				fireStationAgentCoalitions = createNewCoalitions(coalitionFormingAgent);
			}
			else { // agent is joining the existing coalition
				for(AgentCoalitionEntity fireStationAgentCoalition : fireStationAgentCoalitions) {
				  CoalitionEntity fireStationCoalition = fireStationAgentCoalition.getCoalition();
  			  AgentEntity agent = agentPersister.getAgent(coalitionFormingAgent.getId());
  				fireStationCoalition.addAgent(agent);
  				agent.addFormingCoalition(fireStationAgentCoalition);
  				
  				AgentCoalitionEntity agentCoalition = new AgentCoalitionEntity()
  				                                        .withCoalition(fireStationCoalition).withAgent(agent);
  				
  				if(fireStationCoalition.getStatus() != CoalitionStatus.FORMING) {
  				  fireStationCoalition.setStatus(CoalitionStatus.FORMING);
  				}
  				coalitionPersister.save(fireStationCoalition);
  				agentPersister.save(agent);
  				agentCoalitionPersister.save(agentCoalition);
  				LOGGER.info("Agent: "+agent.getId()+" join the coalition: "+fireStationCoalition.getId());
				}
			}
		}
		
		return coalitionFormingAgents;
	}
	
	private List<AgentCoalitionEntity> createNewCoalitions(AgentPlayer player) {
		FireStationEntity fireStation = player.getAgentModel().getFireStation();
		List<FireEntity> closeFires = player.getAgentModel().getCloseFires();
		List<AgentCoalitionEntity> agentCoalitions = new ArrayList<>();
		for(FireEntity closeFire : closeFires) {
		  ForestEntity burningBuilding = closeFire.getBurningForest();
		  // get the agent entity for the player
		  AgentEntity agent = agentPersister.getAgent(player.getId());
		  
		  CoalitionEntity coalition = getCoalition(fireStation);
		  
		  CoalitionForestEntity coalBuildEntity = new CoalitionForestEntity()
		      .withForest(burningBuilding)
		      .withCoalition(coalition);
		  coalition.addCoalitionForest(coalBuildEntity);
		  burningBuilding.addCoalitionForests(coalBuildEntity);
		  
		  // add himself to the coalition
		  coalition.addAgent(agent);
		  
		  AgentCoalitionEntity agentCoalition = new AgentCoalitionEntity()
		                                          .withAgent(agent).withCoalition(coalition);
		  agentCoalitions.add(agentCoalition);
		  agent.addFormingCoalition(agentCoalition);
		  
		  coalitionPersister.save(coalition);
		  buildingPersister.save(burningBuilding);
		  coalitionBuildingPersister.save(coalBuildEntity);
		  agentCoalitionPersister.save(agentCoalition);
		  agentPersister.save(agent);
		  
		  LOGGER.info("create new coalition: "+coalition);
		  
		}
    
		return agentCoalitions;
	}
	
	private List<AgentCoalitionEntity> fireStationCoalition(AgentPlayer player) {
		FireStationEntity fireStation = fireStationPersister.getFireStation(player.getAgentModel().getFireStation().getId());
		List<AgentEntity> fireStationAgents = fireStation.getAgents();
		for(AgentEntity fireStationAgent : fireStationAgents) {
			if(fireStationAgent.getFormingCoalitions() != null && !fireStationAgent.getFormingCoalitions().isEmpty()) {
				return fireStationAgent.getFormingCoalitions();
			}
		}
		return null;
	}
	
	private void setFeasibleCoalitions() {
		List<CoalitionEntity> coalitions = coalitionPersister.getOptimizingCoalitions();
		for(CoalitionEntity coalition : coalitions) {
		  // fire and smoke associated with all the buildings
			double fireAmount = coalitionCalculator.getFireAndSmokeAmount(coalition); 
			double waterAmount = coalitionCalculator.getWaterAmount(coalition);
			if(waterAmount < fireAmount) {
				coalition.setFeasible(false);
			}
			else {
				coalition.setFeasible(true);
			}
			List<CoalitionForestEntity> coalBuilds = coalitionBuildingPersister.getCoalitionBuildings(coalition);
	    for(CoalitionForestEntity coalBuild : coalBuilds) {
	      if(coalBuild.getResourceAmount() >= coalBuild.getTaskAmount()) {
	        coalBuild.setStatus(CoalitionForestStatus.FEASIBLE);
	        coalitionBuildingPersister.save(coalBuild);
	      }
	    }
			coalitionPersister.save(coalition);
			LOGGER.info("coalition: "+coalition.getId()+" status: "+coalition.getStatus()+" feasible: "+coalition.getFeasible());
		}
	}
	
	private void enabledBestCoalitionToExecute() {
	  List<CoalitionForestEntity> allocatedCoalBuilds = null;
	  // 1. Find burning buildings that has only one coalition
	  List<ForestEntity> oneCoalitionBuildings = coalitionBuildingPersister.getOneCoalitionBuildings();
	  while(!oneCoalitionBuildings.isEmpty()) {
  	  // 2. Allocate single coalition to a single building
  	  allocatedCoalBuilds = allocateSingleCoalitions(oneCoalitionBuildings);
  	  // 3. Cancel any other agent related coalition by the same fire station
  	  coalitionCleaner.cancelSingleBuildingOtherCoalitions(allocatedCoalBuilds);
  	  oneCoalitionBuildings = coalitionBuildingPersister.getOneCoalitionBuildings();
	  }
	  // 4. Get all the feasible coalitions
	  List<CoalitionEntity> feasibleUnallocatedCoalitions = coalitionPersister.getFeasibleUnallocatedCoalitions();
	  if(feasibleUnallocatedCoalitions == null || feasibleUnallocatedCoalitions.isEmpty()) {
	    return;
	  }
	  // 5. Allocated the best coalition to execute the task on the building
	  allocatedCoalBuilds = allocateBestCoalitionToTask(feasibleUnallocatedCoalitions);
	  // 6. Discard all the unwanted [coalition - building] pairs created for an allocated building
	  coalitionCleaner.cancelUnwantedCoalBuildForBuildings(allocatedCoalBuilds);
	  // 7. Discard all the unwanted [coalition - building] pairs created for an executed coalition
	  coalitionCleaner.cancelUnwantedCoalBuildForCoalitions(allocatedCoalBuilds);
	  // 8. Discard all the unwanted coalitions, where all the [coalition - building] pair a coalition has is cancelled
	  Set<AgentPlayer> unwantedPlayers = coalitionCleaner.cancelUnwantedCoalitions();
	  // 9. Release agents and players
	  unwantedAgentPlayers.addAll(unwantedPlayers);
	}
	
	private List<CoalitionForestEntity> allocateSingleCoalitions(List<ForestEntity> oneCoalitionBuildings) {
	  List<CoalitionForestEntity> allocatedCoalBuilds = new ArrayList<>();
	  for(ForestEntity oneCoalitionBuilding : oneCoalitionBuildings) {
	    // 1. If the buildings has multiple coal_build pairs, then ignore
	    List<CoalitionForestEntity> coalBuilds = oneCoalitionBuilding.getUtilizedCoalitionForests();
	    if(coalBuilds.size() > 1) {
	      continue;
	    }
	    CoalitionForestEntity coalBuild = coalBuilds.get(0);
	    // 2. If the coalition is not feasible, then ignore
	    CoalitionEntity bestCoalition = coalBuild.getCoalition();
	    if(bestCoalition.getFeasible() == null || bestCoalition.getFeasible() == false) {
	      continue;
	    }
	    // 3. Assign the building to the coalition
	    bestCoalition.setAllocatedForest(coalBuild.getForest());
	    coalitionPersister.save(bestCoalition);
	    coalBuild.setStatus(CoalitionForestStatus.ALLOCATED);
	    coalitionBuildingPersister.save(coalBuild);
	    // 4. Confirm the agents about the best coalition
	    for(AgentEntity agent : bestCoalition.getAgents()) {
	      agent.setCoalition(bestCoalition);
	      agentPersister.save(agent);
	    }
	    // 4. Set the best coalition to execute the task
	    allocatedCoalitions.add(bestCoalition);
	    allocatedCoalBuilds.add(coalBuild);
	  }
	  return allocatedCoalBuilds;
	}

  private List<CoalitionForestEntity> allocateBestCoalitionToTask(List<CoalitionEntity> feasibleUnallocatedCoalitions) {
    List<CoalitionForestEntity> bestCoalBuilds = new ArrayList<>();
	  for(CoalitionEntity feasibleCoalition : feasibleUnallocatedCoalitions) {
	    CoalitionForestEntity bestUtilCoalBuild = setupBestCoalition(feasibleCoalition);
	    if(bestUtilCoalBuild == null) {
	      continue;
	    }
	    bestCoalBuilds.add(bestUtilCoalBuild);
	  }
    return bestCoalBuilds;
  }
	
	private CoalitionForestEntity setupBestCoalition(CoalitionEntity feasibleCoalition) {
	  CoalitionForestEntity bestCoalBuild = null;
	  CoalitionEntity bestUtilityCoalition = null;
	  // 1. get the buildings that the coalition has find it interesting
	  List<ForestEntity> burningBuilds = coalitionBuildingPersister.getBuildings(feasibleCoalition);
    for(ForestEntity burningBuild : burningBuilds) {
      // 2. The building has only one coalition assigned, then there is no further investigation
      List<CoalitionForestEntity> coalBluildings = coalitionBuildingPersister.getCoalitionBuildings(burningBuild);
      if(coalBluildings.size() == 1) {
        bestCoalBuild = coalBluildings.get(0);
        continue;
      }
      // 3. if burning build already has a coalition allocated; continue
      CoalitionForestEntity allocatedCoalBuild = coalitionBuildingPersister.getAllocatedCoalBuild(burningBuild);
      if(allocatedCoalBuild != null) {
        continue;
      }
      // 4. Find the highest utility coalition
      List<CoalitionForestEntity> coalBuilds = coalitionBuildingPersister.findBestUtilityCoalitionBuildings(burningBuild);
      // 4.1. Only one highest coal-building relation
      if(coalBuilds.size() == 1) { 
        bestCoalBuild = coalBuilds.get(0);
      }
      // 4.2 More than one coalition has the same utility, use randomization to select one
      else if(coalBuilds.size() > 1) {  
        bestCoalBuild = coalBuilds.get(random.nextInt(coalBuilds.size()));
      }
      // 4.3. Otherwise this is an exception
      else {
        throw new ManageDisasterServiceException("Can not find a coalition that form to extinguish the fire. Building: "+burningBuild);
      }
    }
    if(bestCoalBuild == null) {
      return null;
    }
    bestUtilityCoalition = bestCoalBuild.getCoalition();
    // 5. Assign the selected building to the coalition
    bestUtilityCoalition.setAllocatedForest(bestCoalBuild.getForest());
    coalitionPersister.save(bestUtilityCoalition);
    bestCoalBuild.setStatus(CoalitionForestStatus.ALLOCATED);
    coalitionBuildingPersister.save(bestCoalBuild);
    // 6. Agents are assigned the best utility coalition
    for(AgentEntity agent : bestUtilityCoalition.getAgents()) {
      agent.setCoalition(bestUtilityCoalition);
      agentPersister.save(agent);
    }
    // 7. Set the best coalition to execute the task
    allocatedCoalitions.add(bestUtilityCoalition);
    return bestCoalBuild;
	}
	
	private void setCoalitionOptimizingToExecuting(CoalitionEntity coalition) {
	  FireEntity fire = coalition.getAllocatedForest().getFires().get(0);
	  fire.setCoalition(coalition);
	  firePersister.saveFire(fire);
	  
	  coalition.setStatus(CoalitionStatus.EXECUTING);
	  coalitionPersister.save(coalition);
	  
	  CoalitionForestEntity allocatedCoalBuild = coalitionBuildingPersister.getAllocatedCoalBuild(coalition);
	  allocatedCoalBuild.setStatus(CoalitionForestStatus.EXECUTING);
	  coalitionBuildingPersister.save(allocatedCoalBuild);
	  
	  // Need to set agent players to execute the task
	  List<AgentEntity> entities = coalition.getAgents();
	  for(AgentEntity entity : entities) {
	    AgentPlayer player = agentSociety.getAgent(entity.getId());
	    player.getAgentModel().setCoalition(coalition);
	    player.getAgentModel().setCloseFires(null);
	    player.getAgentModel().setFire(fire);
	    player.setAgentFromFCtoT();
	  }
	  // Need to clean the agents
	  coalitionCleaner.clearAgent(entities);
	}

  private CoalitionEntity getCoalition(FireStationEntity fireStation) {
    CoalitionEntity coalition = new CoalitionEntity()
          .withX(fireStation.getRoadX())
          .withY(fireStation.getRoadY())
          .withStatus(CoalitionStatus.INITIATING)
          .withFeasible(true)
          .withFireStation(fireStation);
    return coalition;
  }
}
