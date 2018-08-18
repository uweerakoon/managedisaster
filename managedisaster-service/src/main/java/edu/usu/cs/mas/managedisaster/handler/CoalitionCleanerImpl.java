package edu.usu.cs.mas.managedisaster.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionForestPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class CoalitionCleanerImpl implements CoalitionCleaner {

  @Inject
  private CoalitionForestPersister coalitionForestPersister;
  @Inject
  private CoalitionPersister coalitionPersister;
  @Inject
  private AgentSociety agentSociety;
  @Inject
  private AgentCoalitionPersister agentCoalitionPersister;
  
  @Override
  public void cancelUnwantedCoalForestForForests(List<CoalitionForestEntity> allocatedCoalForests) {
    List<Long> allocatedForestIds = allocatedCoalForests.stream()
                                    .map(acb -> acb.getForest().getId())
                                    .collect(Collectors.toList());
    List<CoalitionForestEntity> coalForestEntities = coalitionForestPersister.getUnallocatedCoalForestByForestIds(allocatedForestIds);
    for(CoalitionForestEntity coalForestEntity : coalForestEntities) {
      coalitionForestPersister.cancel(coalForestEntity);
    }
  }
  
  @Override
  public void cancelUnwantedCoalForestForCoalitions(List<CoalitionForestEntity> allocatedCoalForests) {
    List<Long> allocatedCoalIds = allocatedCoalForests.stream()
                                    .map(acf -> acf.getCoalition().getId())
                                    .collect(Collectors.toList());
    List<CoalitionForestEntity> coalForestEntities = coalitionForestPersister.getUnallocatedCoalForestByCoalIds(allocatedCoalIds);
    for(CoalitionForestEntity coalForestEntity : coalForestEntities) {
      coalitionForestPersister.cancel(coalForestEntity);
    }
  }
  
  @Override
  public Set<AgentPlayer> cancelUnwantedCoalitions() {
    Set<AgentPlayer> unwantedAgentPlayers = new HashSet<>();
    List<Integer> cancelCoalitionIds =  coalitionForestPersister.getCancelledCoalitionIds();
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
  public void cancelSingleForestOtherCoalitions(List<CoalitionForestEntity> allocatedCoalForests) {
    for(CoalitionForestEntity allocatedCoalForest : allocatedCoalForests) {
      // 1. collect allocated information
      CoalitionEntity allocatedCoal = allocatedCoalForest.getCoalition();
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
        // 6. cancel all its coal_forest pairs
        List<CoalitionForestEntity> unallocatedCoalForests = coalitionForestPersister.getCoalitionForests(unallocatedFireStationCoal);
        for(CoalitionForestEntity unallocatedCoalForest : unallocatedCoalForests) {
          coalitionForestPersister.cancel(unallocatedCoalForest);
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
