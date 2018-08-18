package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;
import java.util.Set;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface CoalitionCleaner {
  
  public void cancelUnwantedCoalBuildForBuildings(List<CoalitionForestEntity> allocatedCoalBuilds);
  
  public void cancelUnwantedCoalBuildForCoalitions(List<CoalitionForestEntity> allocatedCoalBuilds);
  
  public Set<AgentPlayer> cancelUnwantedCoalitions();
  
  public void cancelSingleBuildingOtherCoalitions(List<CoalitionForestEntity> allocatedCoalBuilds);
  
  public void clearAgent(List<AgentEntity> allocatedAgents);
  
}
