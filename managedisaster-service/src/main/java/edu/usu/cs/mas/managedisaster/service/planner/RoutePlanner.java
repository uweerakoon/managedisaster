package edu.usu.cs.mas.managedisaster.service.planner;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import sim.util.MutableInt2D;

public interface RoutePlanner {
  
  public Route createRoute(AgentPlayer agent);
  
  /** 
   * Get route for the corresponding agent player
   * @param agentPlayer
   * @return Route
   */
  public Route getRoute(AgentPlayer agentPlayer);
  
  public void releaseAgent(AgentPlayer agent);
  
  public Route createRouteToReachFireStation(AgentPlayer agent);
  
  public MutableInt2D findClosestRoadCoordinate(MutableInt2D fireHotSpot, ForestEntity fireForest);
}
