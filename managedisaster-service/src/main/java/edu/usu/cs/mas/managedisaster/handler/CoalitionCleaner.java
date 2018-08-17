package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;
import java.util.Set;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface CoalitionCleaner {
  
  public void cancelUnwantedCoalBuildForBuildings(List<CoalitionBuildingEntity> allocatedCoalBuilds);
  
  public void cancelUnwantedCoalBuildForCoalitions(List<CoalitionBuildingEntity> allocatedCoalBuilds);
  
  public Set<AgentPlayer> cancelUnwantedCoalitions();
  
  public void cancelSingleBuildingOtherCoalitions(List<CoalitionBuildingEntity> allocatedCoalBuilds);
  
  public void clearAgent(List<AgentEntity> allocatedAgents);
  
}
