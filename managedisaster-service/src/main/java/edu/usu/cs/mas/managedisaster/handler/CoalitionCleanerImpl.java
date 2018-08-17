package edu.usu.cs.mas.managedisaster.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.entity.AgentCoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class CoalitionCleanerImpl implements CoalitionCleaner {

  @Inject
  private CoalitionBuildingPersister coalitionBuildingPersister;
  @Inject
  private CoalitionPersister coalitionPersister;
  @Inject
  private AgentSociety agentSociety;
  @Inject
  private AgentCoalitionPersister agentCoalitionPersister;
  
  @Override
  public void cancelUnwantedCoalBuildForBuildings(List<CoalitionBuildingEntity> allocatedCoalBuilds) {
    List<Long> allocatedBuildingIds = allocatedCoalBuilds.stream()
                                    .map(acb -> acb.getBuilding().getId())
                                    .collect(Collectors.toList());
    List<CoalitionBuildingEntity> coalBuildEntities = coalitionBuildingPersister.getUnallocatedCoalBuildByBuildIds(allocatedBuildingIds);
    for(CoalitionBuildingEntity coalBuildEntity : coalBuildEntities) {
      coalitionBuildingPersister.cancel(coalBuildEntity);
    }
  }
  
  @Override
  public void cancelUnwantedCoalBuildForCoalitions(List<CoalitionBuildingEntity> allocatedCoalBuilds) {
    List<Long> allocatedCoalIds = allocatedCoalBuilds.stream()
                                    .map(acb -> acb.getCoalition().getId())
                                    .collect(Collectors.toList());
    List<CoalitionBuildingEntity> coalBuildEntities = coalitionBuildingPersister.getUnallocatedCoalBuildByCoalIds(allocatedCoalIds);
    for(CoalitionBuildingEntity coalBuildEntity : coalBuildEntities) {
      coalitionBuildingPersister.cancel(coalBuildEntity);
    }
  }
  
  @Override
  public Set<AgentPlayer> cancelUnwantedCoalitions() {
    Set<AgentPlayer> unwantedAgentPlayers = new HashSet<>();
    List<Integer> cancelCoalitionIds =  coalitionBuildingPersister.getCancelledCoalitionIds();
    for(Integer cancelCoalitionId : cancelCoalitionIds) {
      CoalitionEntity cancelCoalition = coalitionPersister.getCoalition((long)cancelCoalitionId);
      // release agents
      List<AgentEntity> agentEntities = cancelCoalition.getAgents();
      List<Long> agentIds = agentEntities.stream()
                              .map(ag -> ag.getId())
                              .collect(Collectors.toList());
      List<AgentPlayer> unwantedPlayers = agentSociety.getAgents(agentIds);
      unwantedAgentPlayers.addAll(unwantedPlayers);
      coalitionPersister.cancel(cancelCoalition);
      clearAgent(agentEntities);
    }
    return unwantedAgentPlayers;
  }
  
  @Override
  public void cancelSingleBuildingOtherCoalitions(List<CoalitionBuildingEntity> allocatedCoalBuilds) {
    for(CoalitionBuildingEntity allocatedCoalBuild : allocatedCoalBuilds) {
      // 1. collect allocated information
      CoalitionEntity allocatedCoal = allocatedCoalBuild.getCoalition();
      FireStationEntity allocatedFireStation = allocatedCoal.getFireStation();
      List<AgentEntity> allocatedAgents = allocatedCoal.getAgents();
      // 2. find unallocated for the fire station
      List<CoalitionEntity> unallocatedFireStationCoals = coalitionPersister.getUnallocatedCoalitions(allocatedFireStation);
      for(CoalitionEntity unallocatedFireStationCoal : unallocatedFireStationCoals) {
        List<AgentEntity> unAllocatedAgents = unallocatedFireStationCoal.getAgents();
        // 3. find if unallocated agents and allocated agents are common
        @SuppressWarnings("unchecked") 
        Collection<AgentEntity> commonAgents = CollectionUtils.intersection(unAllocatedAgents, allocatedAgents);
        // 4. when no intersection, then ignore
        if(commonAgents.isEmpty()) {
          continue;
        }
        // 5. cancel the coalition
        coalitionPersister.cancel(unallocatedFireStationCoal);
        // 6. cancel all its coal_build pairs
        List<CoalitionBuildingEntity> unallocatedCoalBuilds = coalitionBuildingPersister.getCoalitionBuildings(unallocatedFireStationCoal);
        for(CoalitionBuildingEntity unallocatedCoalBuild : unallocatedCoalBuilds) {
          coalitionBuildingPersister.cancel(unallocatedCoalBuild);
        }
      }
    }
  }
  
  @Override
  public void clearAgent(List<AgentEntity> allocatedAgents) {
    for(AgentEntity allocatedAgent : allocatedAgents) {
      agentCoalitionPersister.delete(allocatedAgent);
      allocatedAgent.clearFormingCoalition();
    }
  }
}
