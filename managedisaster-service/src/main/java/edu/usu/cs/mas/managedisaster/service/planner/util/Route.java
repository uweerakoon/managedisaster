package edu.usu.cs.mas.managedisaster.service.planner.util;

import java.util.ArrayList;
import java.util.List;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.common.AgentMove;
import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;

@AutoProperty
public class Route {
  private static final int MAXIMUM_PROXIMITY = 1;
  private List<RouteStep> routeSteps;
  private RouteStep currentStep;
  private MutableInt2D source;
  private IntersectionEntity destination;
  private MutableInt2D finalReach;
  private int expectedArrivalTime;
  private AgentMove agentMove; // Determine whether the agent is so close to the target or on the target
  
  private int index = 0;
  
  public List<RouteStep> getRouteSteps() {
    return routeSteps;
  }
  public void setRouteSteps(List<RouteStep> routeSteps) {
    this.routeSteps = routeSteps;
  }
  public Route withRouteSteps(List<RouteStep> routeSteps) {
    setRouteSteps(routeSteps);
    return this;
  }
  public void addRouteStep(RouteStep routeStep){
    if(routeSteps == null){
      routeSteps = new ArrayList<>();
    }
    routeSteps.add(routeStep);
  }
  public void nextStep(){
    if(routeSteps.size() == (index+1)){ // index cannot be increased more
      return;
    }
    index++;
    currentStep = routeSteps.get(index);
  }
  public RouteStep getCurrentStep() {
    if(currentStep == null){
      currentStep = routeSteps.get(index);
      index++;
    }
    return currentStep;
  }
  public void setCurrentStep(RouteStep currentStep) {
    this.currentStep = currentStep;
  }
  public Route withCurrentStep(RouteStep currentStep) {
    setCurrentStep(currentStep);
    return this;
  }
  public MutableInt2D getSource() {
    return source;
  }
  public void setSource(MutableInt2D source) {
    this.source = source;
  }
  public Route withSource(MutableInt2D source) {
    setSource(source);
    return this;
  }
  public IntersectionEntity getDestination() {
    return destination;
  }
  public void setDestination(IntersectionEntity destination) {
    this.destination = destination;
  }
  public Route withDestination(IntersectionEntity destination) {
    setDestination(destination);
    return this;
  }
  public int getExpectedArrivalTime() {
    return expectedArrivalTime;
  }
  public void setExpectedArrivalTime(int expectedArrivalTime) {
    this.expectedArrivalTime = expectedArrivalTime;
  }
  public Route withExpectedArrivalTime(int expectedArrivalTime) {
    setExpectedArrivalTime(expectedArrivalTime);
    return this;
  }
  
  public MutableInt2D getFinalReach() {
    return finalReach;
  }
  public void setFinalReach(MutableInt2D finalReach) {
    this.finalReach = finalReach;
  }
  public Route withFinalReach(MutableInt2D finalReach) {
    setFinalReach(finalReach);
    return this;
  }
  
  public boolean reachDestination(double x, double y) {
    int finalX = finalReach != null ? finalReach.x : destination.getX();
    int finalY = finalReach != null ? finalReach.y : destination.getY();
    double distance = Math.sqrt(
        ((finalX - x) * (finalX - x))
        + ((finalY - y) * (finalY - y)));
    if(distance < MAXIMUM_PROXIMITY) {
      return true;
    }
    return false;
  }
  
  public AgentMove getAgentMove() {
		return agentMove;
	}
  
	public void setAgentMove(AgentMove agentMove) {
		this.agentMove = agentMove;
	}
	
	public Route withAgentMove(AgentMove agentMove) {
		setAgentMove(agentMove);
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
