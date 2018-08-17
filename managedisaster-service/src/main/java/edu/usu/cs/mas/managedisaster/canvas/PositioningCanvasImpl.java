package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.avatar.AgentAvatar;
import edu.usu.cs.mas.managedisaster.avatar.FireEngineAvatar;
import edu.usu.cs.mas.managedisaster.avatar.FireTruckAvatar;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import sim.util.Double2D;

public class PositioningCanvasImpl implements PositioningCanvas{

  private final Continuous2D positions = new Continuous2D(1.0, 100, 100);
  
  @Override
  public Continuous2D getPositions(){
    return positions;
  }
  
  @Override
  public void clearPositions(){
    positions.clear();
  }
  
  @Override
  public void setAgentLocation(AgentAvatar agentAvatar) {
  	int x = agentAvatar.getAgentPlayer().getX();
  	int y = agentAvatar.getAgentPlayer().getY();
    positions.setObjectLocation(agentAvatar, new Double2D(x, y));
  }
  
  @Override
  public void setFireEngineLocation(FireEngineAvatar fireEngineAvatar) {
  	int x = fireEngineAvatar.getFireEnginePlayer().getX();
  	int y = fireEngineAvatar.getFireEnginePlayer().getY();
  	positions.setObjectLocation(fireEngineAvatar, new Double2D(x, y));
  }
  
  @Override
  public void setFireTruckLocation(FireTruckAvatar fireTruckAvatar) {
  	int x = fireTruckAvatar.getFireTruckPlayer().getX();
  	int y = fireTruckAvatar.getFireTruckPlayer().getY();
  	positions.setObjectLocation(fireTruckAvatar, new Double2D(x, y));
  }
  
  @Override
  public Double2D getCurrentLocation(AgentAvatar agentAvatar) {
    return positions.getObjectLocation(agentAvatar);
  }
  
  @Override
  public double getHeight(){
    return positions.getHeight();
  }
  
  @Override
  public double getWidth(){
    return positions.getWidth();
  }
  
  @Override
  public Bag getNeighbors(Double2D currentPosition, int distance) {
  	Bag neighboringObjects = positions.getNeighborsWithinDistance(currentPosition, distance);
  	return neighboringObjects;
  }
  
  @Override
  public Bag getObjectsByLocation(Double2D location) {
  	Bag objects = positions.getObjectsAtLocation(location);
  	return objects;
  }
}
