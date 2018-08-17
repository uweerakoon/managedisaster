package edu.usu.cs.mas.managedisaster.service.planner.util;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import edu.usu.cs.mas.managedisaster.common.RoadOrientation;
import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

@AutoProperty
public class RouteStep {
  
  private RoadOrientation orientation;
  private int currentLength;
  private MutableInt2D currentPosition;
  private MutableInt2D destination;
  public RoadOrientation getOrientation() {
    return orientation;
  }
  public void setOrientation(RoadOrientation orientation) {
    this.orientation = orientation;
  }
  public RouteStep withOrientation(RoadOrientation orientation) {
    setOrientation(orientation);
    return this;
  }
  public int getCurrentLength() {
    return currentLength;
  }
  public void setCurrentLength(int currentLength) {
    this.currentLength = currentLength;
  }
  public RouteStep withCurrentLength(int currentLength) {
    setCurrentLength(currentLength);
    return this;
  }
  public MutableInt2D getCurrentPosition() {
    return currentPosition;
  }
  public void setCurrentPosition(MutableInt2D currentPosition) {
    this.currentPosition = currentPosition;
  }
  public RouteStep withCurrentPosition(MutableInt2D currentPosition) {
    setCurrentPosition(currentPosition);
    return this;
  }
  public MutableInt2D getDestination() {
    return destination;
  }
  public void setDestination(MutableInt2D destination) {
    this.destination = destination;
  }
  public RouteStep withDestination(MutableInt2D destination) {
    setDestination(destination);
    return this;
  }

  @Override
  public String toString(){
    return Pojomatic.toString(this);
  }
  
  @Override
  public int hashCode(){
    return Pojomatic.hashCode(this);
  }
  
  @Override
  public boolean equals(Object other){
    return Pojomatic.equals(this, other);
  }
}
