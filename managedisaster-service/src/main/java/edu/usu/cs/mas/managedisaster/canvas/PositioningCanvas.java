package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.avatar.AgentAvatar;
import edu.usu.cs.mas.managedisaster.avatar.FireEngineAvatar;
import edu.usu.cs.mas.managedisaster.avatar.FireTruckAvatar;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import sim.util.Double2D;

public interface PositioningCanvas {
  /**
   * Return the basic layer that agents positioned
   * @return position canvas
   */
  public Continuous2D getPositions();
  /**
   * Clears any of the assignments to the positioning system
   */
  public void clearPositions();
  /**
   * Add an agent to the positioning system
   * @param agent
   */
  public void setAgentLocation(AgentAvatar agentAvatar);
  
  /**
   * Returns the current location of the agent
   * @param agent
   * @return agnet's location
   */
  public Double2D getCurrentLocation(AgentAvatar agentAvatar);
  
  /**
   * Returns the height of the canvas
   * @return
   */
  public double getHeight();
  
  /**
   * Returns the width of the canvas
   * @return
   */
  public double getWidth();
  /**
   * Returns the neighboring objects of the given location
   * @param currentPosition
   * @param distance
   * @return
   */
  public Bag getNeighbors(Double2D currentPosition, int distance);
  /**
   * Return the objects at a given location
   * @param location given location
   * @return bag of objects
   */
  public Bag getObjectsByLocation(Double2D location);
  
  /**
   * Set the position of the fire engine
   * @param fireEngineAvatar fire engine avatar
   */
  public void setFireEngineLocation(FireEngineAvatar fireEngineAvatar);
  
  /**
   * Set the position fo the fire truck
   * @param fireTruckAvatar fire truck avatar
   */
  public void setFireTruckLocation(FireTruckAvatar fireTruckAvatar);
}
