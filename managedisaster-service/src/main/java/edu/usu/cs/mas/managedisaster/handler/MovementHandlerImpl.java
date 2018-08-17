package edu.usu.cs.mas.managedisaster.handler;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.avatar.AgentAvatar;
import edu.usu.cs.mas.managedisaster.canvas.PositioningCanvas;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.TargetAgentOrientation;
import edu.usu.cs.mas.managedisaster.exception.ManageDisasterServiceException;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlanner;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import edu.usu.cs.mas.managedisaster.service.planner.util.RouteStep;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

public class MovementHandlerImpl implements MovementHandler{
  
  private static final Logger LOGGER = Logger.getLogger(MovementHandlerImpl.class);
  
  private static final MersenneTwisterFast RANDOM = new MersenneTwisterFast(System.currentTimeMillis());
  private static final int SLOTS_AROUND_TARGET = 3;
  
  private static final int SIMULATION_WIDTH = 100;
  private static final int SIMULATION_HEIGHT = 100;
  private static final int DIAMETER = 1;
  
  @Inject
  private RoutePlanner routePlanner;
  @Inject
  private PositioningCanvas positioningCanvas;
  
  @Override
  public MutableDouble2D getNewLocation(MutableDouble2D currentLocation,
      MutableDouble2D velocity,
      MutableDouble2D targetLocation,
      Double speed) {
    MutableDouble2D newLocation = currentLocation;
    
    velocity.subtract(targetLocation, currentLocation);
    velocity.resize(speed);
    
    if(velocity.length() > speed){
      velocity.resize(speed);
    }
    
    newLocation.add(currentLocation, velocity);
    
    Preconditions.checkNotNull(newLocation, 
      new ManageDisasterServiceException("New Location is null for "
        + "current Location: "+currentLocation
        +" and target: "+targetLocation));
    
    return newLocation;
  }
  
  @Override
  public MutableDouble2D getNewLocation(AgentPlayer agent) {
    Route route = null; 
    RouteStep routeStep = null; 
    MutableDouble2D target = null; 
    
    MutableDouble2D newLocation = agent.getCurrentLocation();
    MutableDouble2D velocity = agent.getVelocity();
    MutableDouble2D currentLocation = agent.getCurrentLocation();
    double speed = agent.getSpeed();
    
    if(agent.getTargetLocation() == null) {
      LOGGER.warn("Trying to get a route information without having a target location");
      return null;
    }
    else {
      route = routePlanner.getRoute(agent);
    }
    
    if(route != null) {
	    routeStep = route.getCurrentStep();
	    target = new MutableDouble2D(routeStep.getDestination().x, routeStep.getDestination().y);
    }
    else { // if the agent is very close or on the target no need to use a route
    	target = new MutableDouble2D(agent.getTargetLocation().x, agent.getTargetLocation().y);
    }
    
    double distance = currentLocation.distance(target);
    if(distance < speed) {
      if(route != null) {
      	route.nextStep();
      }
      return target;
    }
    
    velocity.subtract(target, currentLocation);
    velocity.resize(speed);
    
    if(velocity.length() > speed){
      velocity.resize(speed);
    }
    
    newLocation.add(currentLocation, velocity);
    
    Preconditions.checkNotNull(newLocation, 
      new ManageDisasterServiceException("New Location is null for "
        + "current Location: "+currentLocation
        +" and target: "+target));
    
    newLocation = adjustNewLocationWRTCurrentLocation(newLocation, currentLocation);
    
    return newLocation;
  }

  private MutableDouble2D adjustNewLocationWRTCurrentLocation(MutableDouble2D newLocation, MutableDouble2D currentLocation) {
    // To avoid the agent stuck in the same position for a fractional values
    // agent moves based on the integer values. Check AgentPlayer.move
    if(Math.abs(currentLocation.x - newLocation.x) >= 1
        || Math.abs(currentLocation.y - newLocation.y) >= 1) {
      return newLocation;
    }
    newLocation.x = Math.round(newLocation.x);
    newLocation.y = Math.round(newLocation.y);
    return newLocation;
  }
  
  @Override
  public boolean isValidMove(MutableInt2D newLocation, MutableDouble2D velocity){
    
    if(newLocation.x > SIMULATION_WIDTH) { 
      if (velocity.x > 0) {
          velocity.x = -velocity.x;
      } 
      return false; 
    } else if(newLocation.x < 0) { 
        if (velocity.x < 0){
            velocity.x = -velocity.x;
        } 
        return false; 
    } else if(newLocation.y > SIMULATION_HEIGHT) { 
        if (velocity.y > 0) {
            velocity.y = -velocity.y;
        } 
        return false; 
    } else if(newLocation.y < 0) { 
        if (velocity.y < 0) {
            velocity.y = -velocity.y;
        } 
        return false; 
    }
    return true;
  }
  
  @Override
  public boolean isValidPosition(Int2D position) {
  	if(position.x < 0 || position.x > SIMULATION_WIDTH || position.y < 0 || position.y > SIMULATION_HEIGHT) {
  		return false;
  	}
  	return true;
  }
  
  @Override
  public boolean acceptablePosition( Long agentId, Double2D location ) {
	  if( location.x < DIAMETER/2 || location.x > SIMULATION_WIDTH-DIAMETER/2 ||
	      location.y < DIAMETER/2 || location.y > SIMULATION_WIDTH-DIAMETER/2 ) {
	  	return false;
	  }
	  Bag neighboringObjects = positioningCanvas.getNeighbors( location, 2*DIAMETER );
	  if( neighboringObjects != null ) {
      for( int i = 0 ; i < neighboringObjects.numObjs ; i++ ) {
      	if(neighboringObjects.objs[i] == null) {
      		continue;
      	}
      	Long neighborId = ((AgentAvatar)neighboringObjects.objs[i]).getId();
      	if(neighborId.equals(agentId)) {
      		continue;
      	}
        AgentAvatar neighborObj = (AgentAvatar)(neighboringObjects.objs[i]);
        Double2D neighborObjLoc = positioningCanvas.getCurrentLocation(neighborObj);
        boolean isConflict = conflict(location, neighborObjLoc);
        if(isConflict) {
        	return false;
        }
      }
    }
	  return true;
  }

  private boolean conflict(Double2D a, Double2D b ) {
  	if(a.equals(b)) {
  		return true;
  	}
	  if( ( ( a.x > b.x && a.x < b.x+DIAMETER ) ||
	          ( a.x+DIAMETER > b.x && a.x+DIAMETER < b.x+DIAMETER ) ) &&
	          ( ( a.y > b.y && a.y < b.y+DIAMETER ) ||
	          ( a.y+DIAMETER > b.y && a.y+DIAMETER < b.y+DIAMETER ) ) ) {
	  	return true;	
    }
	  return false;
  }
  
  public Int2D getCloseTargetLocation(AgentPlayer agent, Route route) {
  	int targetOrientationX = -1;
  	int targetOrientationY = -1;
  	if(agent.getStatus() == AgentStatus.TRAVELING) {
  		targetOrientationX = agent.getAgentModel().getFire().getX();
  		targetOrientationY = agent.getAgentModel().getFire().getY();
  	}
  	else if(agent.getStatus() == AgentStatus.TRAVELING_TO_FIRE_STATION) {
  		targetOrientationX = agent.getAgentModel().getFireStation().getStationX();
  		targetOrientationY = agent.getAgentModel().getFireStation().getStationY();
  	}
  	MutableInt2D dest = route.getFinalReach(); 
  	int destX = dest.x;
  	int destY = dest.y;
  	int minX;
		int minY;
		TargetAgentOrientation orientation;
		Int2D position;
		if(targetOrientationY < destY) { // horizontal fire up
  		minX = destX - 1; 
  		minY = destY; 
  		orientation = TargetAgentOrientation.HORIZONTAL_UP;
		}
  	else if(targetOrientationY > destY) { // horizontal fire down
  		minX = destX - 1; 
  		minY = destY - 2;
  		orientation = TargetAgentOrientation.HORIZONTAL_DOWN;
		} 
  	else if (targetOrientationX < destX) { // vertical fire left
  		minX = destX; 
  		minY = destY - 1; 
  		orientation = TargetAgentOrientation.VERTICAL_LEFT;
		}
  	else { 
  		minX = destX - 2; 
  		minY = destY - 1;
  		orientation = TargetAgentOrientation.VERTICLE_RIGHT;
		}
		position = getCloseTargetLocation(agent.getId(), minX, minY, orientation);
		boolean isValidPosition = isValidPosition(position);
		if(!isValidPosition) {
			return null;
		}
		return position;
	}
  
  private Int2D getCloseTargetLocation(Long agentId, int minX, int minY, TargetAgentOrientation orientation) {
  	int maxX = minX + SLOTS_AROUND_TARGET;
  	int maxY = minY + SLOTS_AROUND_TARGET;
  	Int2D location =  null;
  	switch(orientation) {
  		case HORIZONTAL_UP:
  			location = getHorizontalUpLocation(agentId, minX, maxX, minY, maxY);
  			break;
  		case HORIZONTAL_DOWN:
  			location = getHorizontalDownLocation(agentId, minX, maxX, minY, maxY);
  			break;
  		case VERTICAL_LEFT:
  			location = getVerticleLeftLocation(agentId, minX, maxX, minY, maxY);
  			break;
  		case VERTICLE_RIGHT:
  			location = getVerticleRightLocation(agentId, minX, maxX, minY, maxY);
  			break;
  	}
  	if(location == null) {
	  	int x = RANDOM.nextInt(maxX - minX) + minX;
	  	int y = RANDOM.nextInt(maxY - minY) + minY;
	  	location = new Int2D(x,y);
  	}
  	return location;
  }
  
  // Ag (1,2) Fire (1,0)
  private Int2D getHorizontalUpLocation(long agentId, int minX, int maxX, int minY, int maxY) {
  	Int2D location =  null;
  	for(int j = minY; j < maxY; j++) {
  		for(int i = minX; i < maxX; i++) {
  			Bag objects = positioningCanvas.getObjectsByLocation(new Double2D(i,j));
  			if(objects == null
  					|| objects.isEmpty()
  					|| (objects.size() == 1 && ((AgentAvatar)objects.objs[0]).getAgentPlayer().getId() == agentId)) {
  				location = new Int2D(i,j); 
  				return location;
  			}
  		}
  	}
  	return location;
  }
  
  // Ag (1,0) fire (1,2)
  private Int2D getHorizontalDownLocation(long agentId, int minX, int maxX, int minY, int maxY) {
  	Int2D location =  null;
		for(int j = maxY - 1; j >= minY; j--) {
			for(int i = maxX - 1; i >= minX; i--) {
  			Bag objects = positioningCanvas.getObjectsByLocation(new Double2D(i,j));
  			if(objects == null
  					|| objects.isEmpty()
  					|| (objects.size() == 1 && ((AgentAvatar)objects.objs[0]).getAgentPlayer().getId() == agentId)) {
  				location = new Int2D(i,j); 
  				return location;
  			}
  		}
  	}
  	return location;
  }
  
  // Ag (2,2) fire (0,2)
  private Int2D getVerticleLeftLocation(long agentId, int minX, int maxX, int minY, int maxY) {
  	Int2D location =  null;
  	for(int i = minX; i < maxX; i++) {
  		for(int j = minY; j < maxY; j++) {
  			Bag objects = positioningCanvas.getObjectsByLocation(new Double2D(i,j));
  			if(objects == null
  					|| objects.isEmpty()
  					|| (objects.size() == 1 && ((AgentAvatar)objects.objs[0]).getAgentPlayer().getId() == agentId)) {
  				location = new Int2D(i,j); 
  				return location;
  			}
  		}
  	}
  	return location;
  }
  
  // Ag(0,2) fire(2,2)
  private Int2D getVerticleRightLocation(long agentId, int minX, int maxX, int minY, int maxY) {
  	Int2D location =  null;
  	for(int i = maxX - 1; i >= minX; i--) {
  		for(int j = maxY - 1; j >= minY; j--) {
  			Bag objects = positioningCanvas.getObjectsByLocation(new Double2D(i,j));
  			if(objects == null
  					|| objects.isEmpty()
  					|| (objects.size() == 1 && ((AgentAvatar)objects.objs[0]).getAgentPlayer().getId() == agentId)) {
  				location = new Int2D(i,j); 
  				return location;
  			}
  		}
  	}
  	return location;
  }

}
