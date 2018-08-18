package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;
import java.util.Set;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface CoalitionCleaner {
  
  public void cancelUnwantedCoalForestForForests(List<CoalitionForestEntity> allocatedCoalForests);
  
  public void cancelUnwantedCoalForestForCoalitions(List<CoalitionForestEntity> allocatedCoalForests);
  
  public Set<AgentPlayer> cancelUnwantedCoalitions();
  
  public void cancelSingleForestOtherCoalitions(List<CoalitionForestEntity> allocatedCoalForests);
  
  public void clearAgent(List<AgentEntity> allocatedAgents);
  
}
