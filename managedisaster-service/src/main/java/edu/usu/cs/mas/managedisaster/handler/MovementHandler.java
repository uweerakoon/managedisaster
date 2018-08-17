package edu.usu.cs.mas.managedisaster.handler;

import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

public interface MovementHandler {
  /**
   * Handles the logic to get the new location 
   * @param currentLocation
   * @param targetLocation
   * @return new location
   */
  public MutableDouble2D getNewLocation(AgentPlayer agentPlayer);
  public MutableDouble2D getNewLocation(MutableDouble2D currentLocation, 
      MutableDouble2D velocity,
      MutableDouble2D targetLocation,
      Double speed);
  /**
   * Check whether the move is valid or not
   * @param positioningCanvas
   * @param velocity
   * @return
   */
  public boolean isValidMove(MutableInt2D newLocation, MutableDouble2D velocity);
  
  /**
   * Check whether the position is already occupied with another neighboring agent
   * @param agentId
   * @param location
   * @return
   */
  public boolean acceptablePosition( Long agentId, Double2D location );
  
  /**
   * Check whether the position is valid in the simulation
   */
  public boolean isValidPosition(Int2D position);
  
  /**
   * Find the closest target around the base objective location
   * @param fireX
   * @param fireY
   * @param destX
   * @param destY
   * @return
   */
  public Int2D getCloseTargetLocation(AgentPlayer agent, Route route);
}
