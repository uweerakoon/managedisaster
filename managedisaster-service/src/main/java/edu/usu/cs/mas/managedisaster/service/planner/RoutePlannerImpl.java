package edu.usu.cs.mas.managedisaster.service.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;

import com.google.common.annotations.VisibleForTesting;

import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvas;
import edu.usu.cs.mas.managedisaster.common.AgentMove;
import edu.usu.cs.mas.managedisaster.common.RoadOrientation;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadIntersectionEntity;
import edu.usu.cs.mas.managedisaster.persister.IntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadIntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import edu.usu.cs.mas.managedisaster.service.planner.util.RouteStep;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.MutableInt2D;


public class RoutePlannerImpl implements RoutePlanner{

  private static final Logger LOGGER = Logger.getLogger(RoutePlannerImpl.class);
  private static final int MAXIMUM_PROXIMITY = 1;

  private static final boolean DIRECTED_EDGES = false;
  private static final int LENGTH = 100;
  private static final int WIDTH = 100;

  private Network roadNetwork;
  private List<Long> newRoadIds = new ArrayList<>();
  private List<Long> newIntersectionIds = new ArrayList<>();
  private List<Long> newRoadIntersectionIds = new ArrayList<>();
  private List<Edge> removedEdges = new ArrayList<>();
  private List<Edge> addedEdges = new ArrayList<>();
  private List<Long> addedNodes = new ArrayList<>();

  @Inject
  private RoadIntersectionPersister roadIntersectionPersister;
  @Inject
  private IntersectionPersister intersectionPersister;
  @Inject
  private RoadPersister roadPersister;
  @Inject
  private RoadCanvas roadCanvas;
  @Inject
  private ForestCanvas buildingCanvas;

  private Map<Long, Route> routePlans;

  public RoutePlannerImpl() { }

  public RoutePlannerImpl(
    RoadIntersectionPersister roadIntersectionPersister,
    IntersectionPersister intersectionPersister,
    RoadPersister roadPersister,
    RoadCanvas roadCanvas,
    ForestCanvas buildingCanvas){ 

    this.roadIntersectionPersister = roadIntersectionPersister;
    this.intersectionPersister = intersectionPersister;
    this.roadPersister = roadPersister;
    this.roadCanvas = roadCanvas;
    this.buildingCanvas = buildingCanvas;
  }

  @Override
  public Route getRoute(AgentPlayer agentPlayer){
    if(MapUtils.isEmpty(routePlans)){
      return null;
    }
    Route route = routePlans.get(agentPlayer.getId());
    return route;
  }

  @Override
  public Route createRoute(AgentPlayer agent){
    Route route = null;
    setup();
    MutableInt2D targetLocation = agent.getTargetLocation(); // fire location
    MutableInt2D targetRoadLocation = findClosestRoadCoordinate(targetLocation,agent.getAgentModel().getFire().getBurningForest());
    agent.setTargetLocation(targetRoadLocation); // once target location figured out, it lays on the road
    double distance = agent.getCurrentLocation().distance(targetRoadLocation);
    if(distance == 0) {
      route = new Route().withAgentMove(AgentMove.ON_TARGET).withFinalReach(targetRoadLocation);
      return route;
    }
    if(distance <= agent.getSpeed()) {
      route = new Route().withAgentMove(AgentMove.CLOSE_TO_TARGET).withFinalReach(targetRoadLocation);
      return route;
    }
    setupAgentRoadNetwork(agent, targetRoadLocation);
    route = findAndSetupRoute(agent);
    route.setAgentMove(AgentMove.FAR_FROM_TARGET);
    cleanup(agent);
    LOGGER.info("Create route for the agent id: "+agent.getId()+" coordinates: "+agent.getCurrentLocation()+" route: "+route);
    return route;
  }

  @Override
  public Route createRouteToReachFireStation(AgentPlayer agent) {
    Route route = null;
    setup();
    setupAgentRoadNetworkForFireStation(agent);
    route = findAndSetupRoute(agent);
    cleanup(agent);
    LOGGER.info("Create route (Fire Station) for the agent id: "+agent.getId()+" coordinates: "+agent.getCurrentLocation()+" route: "+route);
    return route;
  }

  @Override
  public MutableInt2D findClosestRoadCoordinate(MutableInt2D fireHotSpot, ForestEntity fireBuilding){
    int targetX = fireHotSpot.x;
    int targetY = fireHotSpot.y;
    if(roadCanvas.isRoadCoordinate(targetX, targetY)){
      return fireHotSpot;
    }

    List<DistanceLocation> distanceToRoadCoordinate = new ArrayList<>();

    for(int i=targetX; i<LENGTH; i++){
      if(roadCanvas.isRoadCoordinate(i, targetY)){
        int distance = (int)fireHotSpot.distance(i, targetY);
        distanceToRoadCoordinate.add(new DistanceLocation(distance, new MutableInt2D(i,targetY)));
        break;
      }
    }
    for(int i=targetX; i>0; i--){
      if(roadCanvas.isRoadCoordinate(i, targetY)){
        int distance = (int)fireHotSpot.distance(i, targetY);
        distanceToRoadCoordinate.add(new DistanceLocation(distance, new MutableInt2D(i,targetY)));
        break;
      }
    }
    for(int j=targetY; j<WIDTH; j++){
      if(roadCanvas.isRoadCoordinate(targetX, j)){
        int distance = (int)fireHotSpot.distance(targetX, j);
        distanceToRoadCoordinate.add(new DistanceLocation(distance, new MutableInt2D(targetX, j)));
        break;
      }
    }
    for(int j=targetY; j>0; j--){
      if(roadCanvas.isRoadCoordinate(targetX, j)){
        int distance = (int)fireHotSpot.distance(targetX, j);
        distanceToRoadCoordinate.add(new DistanceLocation(distance, new MutableInt2D(targetX, j)));
        break;
      }
    }

    distanceToRoadCoordinate = distanceToRoadCoordinate
        .stream()
        .sorted((d1,d2) -> d1.getDistance().compareTo(d2.getDistance()))
        .collect(Collectors.<DistanceLocation>toList());

    DistanceLocation closeRoadCoordinationToFire = null;
    for(DistanceLocation tempDistanceLoc : distanceToRoadCoordinate) {
      boolean isTargetCovered = isTargetCovered(fireHotSpot, tempDistanceLoc.getLocation(), fireBuilding);
      if(!isTargetCovered) {
        closeRoadCoordinationToFire = tempDistanceLoc;
        break;
      }
    }
    return closeRoadCoordinationToFire.getLocation();
  }

  private Route findAndSetupRoute(AgentPlayer agent) {
    IntersectionEntity sourceIntersection = null; 
    IntersectionEntity destinationIntersection = null; 
    Route route = null;
    sourceIntersection = agent.getSourceIntersection();
    labelNodesByDijkstraAlgorithm(sourceIntersection, agent.getMap());
    destinationIntersection = agent.getTargetIntersection();
    route = getRouteForAgent(agent, sourceIntersection, destinationIntersection);
    routePlans.put(agent.getId(), route);
    return route;
  }

  @Override
  public void releaseAgent(AgentPlayer agent) {
    if(agent.getMap() == null) {
      return;
    }
    routePlans.remove(agent.getId());
    agent.setMap(null);
    agent.setSourceIntersection(null);
    agent.setTargetIntersection(null);
    agent.setTargetLocation(null);
    LOGGER.info("Clean up route information for agent id: "+agent.getId()+" @ "+agent.getCurrentLocation());
  }

  private void setup() {
    if(this.roadNetwork == null) {
      initializeRoadNetwork();
    }
    newRoadIds.clear();
    newIntersectionIds.clear();
    newRoadIntersectionIds.clear();
    removedEdges.clear();
    addedEdges.clear();
    addedNodes.clear();
  }

  private void cleanup(AgentPlayer agent) {
    Network network = agent.getMap();
    for(Edge addedEdge : addedEdges) {
      network.removeEdge(addedEdge);
    }
    for(Long addedNode : addedNodes) {
      network.removeNode(addedNode);
    }
    for(Edge removedEdge : removedEdges) {
      if(addedNodes.contains(removedEdge.from())
          || addedNodes.contains(removedEdge.to())) {
        continue;
      }
      network.addEdge(removedEdge);
    }
    for(Long roadId : newRoadIds) {
      roadPersister.deleteFromCache(roadId);
    }
    for(Long intersecId : newIntersectionIds) {
      intersectionPersister.deleteFromCache(intersecId);
    }
    for(Long roadIntersecId : newRoadIntersectionIds) {
      roadIntersectionPersister.deleteFromCache(roadIntersecId);
    }
  }

  @VisibleForTesting
  protected Route getRouteForAgent(AgentPlayer agent, IntersectionEntity sourceIntersection,
    IntersectionEntity destinationIntersection){

    if(sourceIntersection == null || destinationIntersection == null) {
      return null;
    }

    Network roadNetwork = validateAndInitializeRoadNetwork(agent, sourceIntersection, destinationIntersection);
    List<IntersectionEntity> path = getPath(sourceIntersection, destinationIntersection, roadNetwork);

    List<RouteStep> routeSteps = new ArrayList<>();
    RouteStep routeStep = null;
    Integer totalLength = 0;

    for(int i=0; i < path.size()-1; i++){
      IntersectionEntity currentIntersection = path.get(i);
      IntersectionEntity nextIntersectoin = path.get(i+1);
      routeStep = getIntermediateRouteStep(roadNetwork, currentIntersection, nextIntersectoin);
      totalLength += routeStep.getCurrentLength();
      routeSteps.add(routeStep);
    }

    routeStep = getLastRouteStep(agent, destinationIntersection);
    if(routeStep != null) {
      routeSteps.add(routeStep);
      totalLength += routeStep.getCurrentLength();
    }

    Route route = getCompleteRoute(agent, sourceIntersection, destinationIntersection, routeSteps, totalLength,
      routeSteps.get(routeSteps.size()-1).getDestination());

    return route;
  }

  private RouteStep getLastRouteStep(AgentPlayer agentPlayer, IntersectionEntity destinationIntersection) {
    if(destinationIntersection == null) {
      return null;
    }
    MutableInt2D agentTarget = agentPlayer.getTargetLocation();
    MutableInt2D currentLocation = new MutableInt2D(destinationIntersection.getX(), destinationIntersection.getY());
    double distance = Math.sqrt(
      ((agentTarget.x - currentLocation.x) * (agentTarget.x - currentLocation.x))
      + ((agentTarget.y - currentLocation.y) * (agentTarget.y - currentLocation.y)));
    if(distance < MAXIMUM_PROXIMITY) {
      return null;
    }
    RoadOrientation roadOrientation = Math.abs(agentTarget.x - currentLocation.x) > MAXIMUM_PROXIMITY ? RoadOrientation.HORIZONTAL : RoadOrientation.VERTICAL;
    RouteStep routeStep = new RouteStep()
        .withOrientation(roadOrientation)
        .withCurrentPosition(currentLocation)
        .withDestination(agentTarget)
        .withCurrentLength((int)distance);

    return routeStep;
  }
  //TODO - need to arrange the destinations correctly, otherwise agents are going zig-zag
  private RouteStep getIntermediateRouteStep(Network roadNetwork, IntersectionEntity currentIntersection, IntersectionEntity nextIntersectoin) {
    Long roadId = (Long) roadNetwork.getEdge(currentIntersection.getId(), nextIntersectoin.getId()).info;
    RoadEntity road = roadPersister.getRoad(roadId);
    MutableInt2D destination = new MutableInt2D(nextIntersectoin.getX(), nextIntersectoin.getY());
    MutableInt2D currentLocation = new MutableInt2D(currentIntersection.getX(), currentIntersection.getY());
    int currentLength = (int) currentLocation.distance(destination);

    RouteStep routeStep = getCurrentRouteStep(road, destination, currentLocation, currentLength);
    return routeStep;
  }

  private Network validateAndInitializeRoadNetwork(AgentPlayer agentPlayer, IntersectionEntity sourceIntersection,
    IntersectionEntity destinationIntersection) {
    if(sourceIntersection == null || destinationIntersection == null) {
      LOGGER.error("Intersections are null. source: "+sourceIntersection+" end: "+destinationIntersection);
      return null;
    }

    if(this.roadNetwork == null) {
      initializeRoadNetwork();
    }

    Network roadNetwork = agentPlayer.getMap() != null 
        ? agentPlayer.getMap()
            : this.roadNetwork;
        return roadNetwork;
  }

  private RouteStep getCurrentRouteStep(RoadEntity road, MutableInt2D destination, MutableInt2D currentLocation,
    int currentLength) {
    RouteStep routeStep = new RouteStep()
        .withOrientation(road.getOrientation())
        .withCurrentPosition(currentLocation)
        .withDestination(destination)
        .withCurrentLength(currentLength);
    return routeStep;
  }

  private Route getCompleteRoute(AgentPlayer agentPlayer, IntersectionEntity sourceIntersection,
    IntersectionEntity destinationIntersection, List<RouteStep> routeSteps, int totalLength,
    MutableInt2D finalReach) {
    if(CollectionUtils.isEmpty(routeSteps) || sourceIntersection == null || destinationIntersection == null) {
      return null;
    }
    Route route = new Route()
        .withRouteSteps(routeSteps)
        .withCurrentStep(routeSteps.get(0))
        .withDestination(destinationIntersection)
        .withSource(new MutableInt2D(sourceIntersection.getX(), sourceIntersection.getY()))
        .withExpectedArrivalTime((int)(totalLength/agentPlayer.getSpeed()))
        .withFinalReach(finalReach);
    return route;
  }

  private void initializeRoadNetwork() {
    this.roadNetwork = new Network(DIRECTED_EDGES); 
    routePlans = new HashMap<>();
    setUpRoadNetwork();
  }

  private List<IntersectionEntity> getPath(IntersectionEntity source, 
    IntersectionEntity destination, Network roadNetwork){

    if(roadNetwork == null){
      roadNetwork = this.roadNetwork;
    }
    else {
      labelNodesByDijkstraAlgorithm(source,roadNetwork);
    }
    List<IntersectionEntity> path = getShortestPathTo(destination);
    return path;
  }

  private void setUpRoadNetwork(){
    for(RoadIntersectionEntity roadIntersection : roadIntersectionPersister.getAllRoadIntersections()){
      roadNetwork.addNode(roadIntersection.getSourceIntersection().getId()); // Duplicate nodes will not be inserted
      roadNetwork.addNode(roadIntersection.getDestinationIntersection().getId());
      Edge edge = new Edge(roadIntersection.getSourceIntersection().getId(), 
        roadIntersection.getDestinationIntersection().getId(), 
        roadIntersection.getRoad().getId());
      roadNetwork.addEdge(edge);
    }
  }

  private void labelNodesByDijkstraAlgorithm(IntersectionEntity sourceIntersection, Network roadNetwork){

    Bag allIntersections = roadNetwork.allNodes;
    for(int i=0; i < allIntersections.numObjs; i++){
      Long intersectionId = (Long) allIntersections.objs[i];
      IntersectionEntity intersection = intersectionPersister.getIntersection(intersectionId); 
      intersection.setPreviousIntersection(null);
      if(intersection.equals(sourceIntersection)){
        intersection.setDistance(0);
      }
      else {
        intersection.setDistance(Integer.MAX_VALUE);
      }
    }

    PriorityQueue<IntersectionEntity> intersectionQueue = new PriorityQueue<>();
    intersectionQueue.add(sourceIntersection);

    while(!intersectionQueue.isEmpty()) {

      IntersectionEntity intermediateIntersection = intersectionQueue.poll();

      Bag adjacentEdges = roadNetwork.getEdges(intermediateIntersection.getId(),null);
      for(int i=0; i < adjacentEdges.numObjs; i++){

        Edge adjacentEdge = (Edge) adjacentEdges.objs[i];
        Long adjacentRoadId = (Long) adjacentEdge.info;
        RoadEntity adjacentRoad = roadPersister.getRoad(adjacentRoadId); 

        Long oppositeIntersectionId = (Long) adjacentEdge.getOtherNode(intermediateIntersection.getId());
        IntersectionEntity oppositeIntersection = intersectionPersister.getIntersection(oppositeIntersectionId);


        int roadLenght = adjacentRoad.getLength();
        int distanceThroughTempIntersection = intermediateIntersection.getDistance() + roadLenght;

        if(distanceThroughTempIntersection < oppositeIntersection.getDistance()){

          intersectionQueue.remove(oppositeIntersection);
          oppositeIntersection.setDistance(distanceThroughTempIntersection);
          oppositeIntersection.setPreviousIntersection(intermediateIntersection);
          intersectionQueue.add(oppositeIntersection);

        }
      }
    }
  }

  private List<IntersectionEntity> getShortestPathTo(IntersectionEntity targetIntersection){
    List<IntersectionEntity> path = new ArrayList<>();
    for(IntersectionEntity intersection = targetIntersection;
        intersection != null; intersection = intersection.getPreviousIntersection()){
      path.add(intersection);
    }
    Collections.reverse(path);
    return path;
  }

  private void setupAgentRoadNetwork(AgentPlayer agent, MutableInt2D targetRoadLocation){
    MutableInt2D currentLocation = new MutableInt2D((int)agent.getCurrentLocation().x, (int)agent.getCurrentLocation().y);
    updateMapAndStatus(agent, currentLocation, targetRoadLocation);
  }

  private void setupAgentRoadNetworkForFireStation(AgentPlayer agent) {
    MutableInt2D currentLocation = new MutableInt2D((int)agent.getCurrentLocation().x, (int)agent.getCurrentLocation().y);
    MutableInt2D targetRoadLocation = agent.getTargetLocation();
    updateMapAndStatus(agent, currentLocation, targetRoadLocation);
  }

  private void updateMapAndStatus(AgentPlayer agent, MutableInt2D currentLocation, MutableInt2D targetRoadLocation) {
    RoadEntity currentRoad = null;
    int roadId = roadCanvas.getRoadId(targetRoadLocation);
    RoadEntity nearRoad = roadPersister.getRoad(Long.valueOf(roadId)); 

    // Determine the target road location as an intersection 
    IntersectionEntity targetIntersection = createNewIntersection(targetRoadLocation);
    // Split the considered road into two portions as two separate roads
    List<RoadEntity> roadsConnectedToTarget = craeteNewRoads(nearRoad, targetIntersection);
    // Get two road - intersection relationships: 1) road-source to new-target 2) new-target to road-destination
    List<RoadIntersectionEntity> roadIntersectionConnectedToTarget = createNewRoadIntersections(nearRoad, 
      targetIntersection, roadsConnectedToTarget);
    Network adjustedRoadMap = getAdjustedRoadMap(agent, nearRoad, roadIntersectionConnectedToTarget);
    agent.setMap(adjustedRoadMap);

    for(RoadEntity newRoad : roadsConnectedToTarget) {
      if(newRoad.contains(currentLocation)) {
        currentRoad = newRoad;
      }
    }
    if(currentRoad == null) {
      int currentRoadId = roadCanvas.getRoadId(currentLocation);
      currentRoad = roadPersister.getRoad(Long.valueOf(currentRoadId));
    }
    IntersectionEntity currentIntersection = createNewIntersection(currentLocation);
    List<RoadEntity> roadsConnectedToCurrent = craeteNewRoads(currentRoad, currentIntersection);
    List<RoadIntersectionEntity> roadIntersectionsConnectedToCurrent = createNewRoadIntersections(currentRoad, currentIntersection, 
      roadsConnectedToCurrent);

    // Remove the edge representing current and target roads, insert the newly created two roads and two road-intersection relationships
    adjustedRoadMap = getAdjustedRoadMap(agent, currentRoad, roadIntersectionsConnectedToCurrent);
    agent.setMap(adjustedRoadMap);
    agent.setSourceIntersection(currentIntersection);
    agent.setTargetIntersection(targetIntersection);
    agent.setTargetLocation(targetRoadLocation); // close road location to the fire
  }

  private Network getAdjustedRoadMap(AgentPlayer agent, RoadEntity nearRoad,
    List<RoadIntersectionEntity> roadIntersectionConnectedToTarget) {

    Network roadNetwork = agent.getMap() == null ? this.roadNetwork : agent.getMap();

    Network adjustedRoadMap = new Network(roadNetwork);

    RoadIntersectionEntity roadIntersectionToBeRemoved = roadIntersectionPersister.getRoadIntersection(nearRoad);
    Edge edgeToBeRemoved = adjustedRoadMap.getEdge(roadIntersectionToBeRemoved.getSourceIntersection().getId(),
      roadIntersectionToBeRemoved.getDestinationIntersection().getId());
    adjustedRoadMap.removeEdge(edgeToBeRemoved);
    removedEdges.add(edgeToBeRemoved);

    for(RoadIntersectionEntity addingRoadIntersection : roadIntersectionConnectedToTarget){
      addNode(adjustedRoadMap, addingRoadIntersection.getSourceIntersection().getId());
      addNode(adjustedRoadMap, addingRoadIntersection.getDestinationIntersection().getId());
      Edge newEdge = new Edge(addingRoadIntersection.getSourceIntersection().getId(), 
        addingRoadIntersection.getDestinationIntersection().getId(),
        addingRoadIntersection.getRoad().getId());
      adjustedRoadMap.addEdge(newEdge);
      addedEdges.add(newEdge);
    }

    return adjustedRoadMap;
  }

  private void addNode(Network network, Long nodeId) {
    if(network.nodeExists(nodeId)) {
      return;
    }
    network.addNode(nodeId);
    addedNodes.add(nodeId);
  }

  private List<RoadIntersectionEntity> createNewRoadIntersections(
    RoadEntity nearRoad, IntersectionEntity targetIntersection, 
    List<RoadEntity> adjustedRoad) {

    List<RoadIntersectionEntity> roadIntersections = new ArrayList<>();

    roadIntersections.add(new RoadIntersectionEntity().withId(roadIntersectionPersister.nextId())
      .withRoad(adjustedRoad.get(0))
      .withSourceIntersection(roadIntersectionPersister.getRoadIntersection(nearRoad).getSourceIntersection())
      .withDestinationIntersection(targetIntersection));

    roadIntersections.add(new RoadIntersectionEntity().withId(roadIntersectionPersister.nextId())
      .withRoad(adjustedRoad.get(1))
      .withSourceIntersection(targetIntersection)
      .withDestinationIntersection(roadIntersectionPersister.getRoadIntersection(nearRoad).getDestinationIntersection()));

    roadIntersectionPersister.addAllToCache(roadIntersections);
    newRoadIntersectionIds.add(roadIntersections.get(0).getId());
    newRoadIntersectionIds.add(roadIntersections.get(1).getId());

    return roadIntersections;

  }

  private IntersectionEntity createNewIntersection(MutableInt2D location){
    int x = location.x;
    int y = location.y;
    Long nextIntersectionId = intersectionPersister.nextId();

    IntersectionEntity intersection = new IntersectionEntity().withId(nextIntersectionId).withX(x).withY(y);

    intersectionPersister.addToCache(intersection);
    newIntersectionIds.add(nextIntersectionId);

    return intersection;
  }

  private List<RoadEntity> craeteNewRoads(RoadEntity road, IntersectionEntity intersection){
    List<RoadEntity> connectedRoads = new ArrayList<RoadEntity>();
    connectedRoads.add(road.clone());
    connectedRoads.add(road.clone());

    int length = Math.abs (
      road.getOrientation() == RoadOrientation.HORIZONTAL
      ? road.getX() - intersection.getX()
          : road.getY() - intersection.getY());

    connectedRoads.get(0).withId(roadPersister.nextId()).withX(road.getX())
    .withY(road.getY())
    .withWidth(road.getWidth()).withOrientation(road.getOrientation())
    .withLength(length);

    length = road.getLength() - length;

    connectedRoads.get(1).withId(roadPersister.nextId()).withX(intersection.getX())
    .withY(intersection.getY())
    .withWidth(road.getWidth()).withOrientation(road.getOrientation())
    .withLength(length);

    roadPersister.addAllToCache(connectedRoads);
    newRoadIds.add(connectedRoads.get(0).getId());
    newRoadIds.add(connectedRoads.get(1).getId());

    return connectedRoads;

  }


  private boolean isTargetCovered(MutableInt2D targetLocation, MutableInt2D selectedLocation, ForestEntity fireBuilding) {
    boolean isXConstant = targetLocation.x == selectedLocation.x;
    boolean isYTargetFar = targetLocation.y > selectedLocation.y;
    boolean isXTargetFar = targetLocation.x > selectedLocation.x;
    int constant = isXConstant ? targetLocation.x : targetLocation.y;

    int initValue = isXConstant
        ? (isYTargetFar ? selectedLocation.y : fireBuilding.getMaxY())
            : (isXTargetFar ? selectedLocation.x : fireBuilding.getMaxX());
        int maximumValue = isXConstant
            ? (isYTargetFar ? fireBuilding.getMinY() : selectedLocation.y)
                : (isXTargetFar ? fireBuilding.getMinX() : selectedLocation.x);
            for(int incre = initValue; incre <= maximumValue; incre++) {
              boolean isOtherBuilding = isXConstant ? buildingCanvas.isForestCoordinate(constant, incre) : buildingCanvas.isForestCoordinate(incre, constant);
              if(isOtherBuilding) {
                return true;
              }
            }

            return false;              
  }

  private class DistanceLocation {
    @Property
    private Integer distance;
    @Property
    private MutableInt2D location;
    public DistanceLocation(Integer distance, MutableInt2D location) {
      this.distance = distance;
      this.location = location;
    }
    public Integer getDistance() {
      return distance;
    }
    public MutableInt2D getLocation() {
      return location;
    }
    @Override 
    public int hashCode() {
      return Pojomatic.hashCode(this);
    }

    @Override 
    public String toString() {
      return Pojomatic.toString(this);
    }

    @Override 
    public boolean equals(Object o) {
      return Pojomatic.equals(this, o);
    }
  }

}
