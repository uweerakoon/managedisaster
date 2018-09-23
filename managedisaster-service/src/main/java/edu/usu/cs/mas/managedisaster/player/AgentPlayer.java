package edu.usu.cs.mas.managedisaster.player;

import java.awt.Color;
import java.util.List;

import org.apache.log4j.Logger;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.Property;
import org.springframework.context.ApplicationContext;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.avatar.AgentAvatar;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireStationCanvas;
import edu.usu.cs.mas.managedisaster.canvas.PositioningCanvas;
import edu.usu.cs.mas.managedisaster.common.AgentMove;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.handler.CoalitionFormation;
import edu.usu.cs.mas.managedisaster.handler.Extinguisher;
import edu.usu.cs.mas.managedisaster.handler.MovementHandler;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.ForestPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlanner;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Network;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

public class AgentPlayer implements Steppable {

  private static final long serialVersionUID = -2159391590290197601L;
  private static final Logger LOGGER = Logger.getLogger(AgentPlayer.class);

  private static final int MAX_ACCEPTABLE_ATTEMPTS = 2;
  private static final int MAX_SEARCH_TIME_AT_INCIDENT = 5;
  private static final String OUT_OF_SERVICE = "X";

  private int acceptableAttempts;
  private int searchTimeAtIncident;
  private boolean waitAtIncident;

  @Property
  private AgentModel agentModel;
  private AgentAvatar agentAvatar;

  private MutableDouble2D velocity = new MutableDouble2D();
  @Property
  private MutableDouble2D currentLocation;
  private MutableInt2D targetLocation; // Destination of the movements - fire, fire-station
  private IntersectionEntity targetIntersection;
  private IntersectionEntity sourceIntersection;
  private MutableInt2D newLocation = new MutableInt2D();
  private Network map;

  private MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());

  private MovementHandler movementHandler;
  private PositioningCanvas positioningCanvas;
  private FirePersister firePersister;
  private ForestPersister forestPersister;
  private FireStationPersister fireStationPersister;
  private FireStationCanvas fireStationCanvas;
  private RoutePlanner routePlanner;
  private Extinguisher extinguisher;
  private Config config;
  private AgentPersister agentPersister;
  private CoalitionFormation coalitionFormation;

  public AgentPlayer() { }

  public AgentPlayer(AgentModel agentModel, ApplicationContext applicationContext){
    setBeans(applicationContext);
    setupAgent(agentModel);
  }

  public AgentPlayer(AgentModel agentModel, MovementHandler movementHandler, PositioningCanvas positioningCanvas, FirePersister firePersister,
    RoutePlanner routePlanner, Extinguisher extinguisher, ForestPersister forestPersister) {
    setupAgent(agentModel);
    this.movementHandler = movementHandler;
    this.positioningCanvas = positioningCanvas;
    this.firePersister = firePersister;
    this.routePlanner = routePlanner;
    this.extinguisher = extinguisher;
    this.forestPersister = forestPersister;
  }

  private void setupAgent(AgentModel agentModel) {
    this.agentModel = agentModel;
    this.agentAvatar = new AgentAvatar(this,config);
    setStatus(agentModel.getInitialStatus());
    currentLocation = new MutableDouble2D(agentModel.getX(), agentModel.getY());
    velocity.setTo(currentLocation);
  }

  private void setBeans(ApplicationContext applicationContext) {
    movementHandler = applicationContext.getBean(MovementHandler.class);
    positioningCanvas = applicationContext.getBean(PositioningCanvas.class);
    firePersister = applicationContext.getBean(FirePersister.class);
    routePlanner = applicationContext.getBean(RoutePlanner.class);
    extinguisher = applicationContext.getBean(Extinguisher.class);
    forestPersister = applicationContext.getBean(ForestPersister.class);
    fireStationPersister = applicationContext.getBean(FireStationPersister.class);
    fireStationCanvas = applicationContext.getBean(FireStationCanvas.class);
    config = applicationContext.getBean(Config.class);
    agentPersister = applicationContext.getBean(AgentPersister.class);
    coalitionFormation = applicationContext.getBean(CoalitionFormation.class);
  }

  @Override
  public void step(SimState simState){
    switch(agentModel.getStatus()) {
      case SEARCHING:
        searchFires();
        break;
      case IDENTIFYING_AND_ASSESSING_RISK:
        // Need to access the history and identify the thread level
        // agent decide what strategy they will be: risk seek, risk neutral or risk averse
        throw new UnsupportedOperationException("Agents are not yet supported for IDENTIFYING_AND_ASSESSING_RISK");
      case ESTIMATING_RESOURCES:
        // estimate the resources available
        // come up with a plan to execute the task
        throw new UnsupportedOperationException("Agents are not yet supported for ESTIMATING_RESOURCES");
      case FORMING_COALITIONS:

        break;
      case TRAVELING:
        checkFireAndChangeStatus(); // check the fire is still active
        if(AgentStatus.TRAVELING != agentModel.getStatus()) {
          return;
        }
        move();
        reachDestination();
        break;
      case EXECUTING_TASKS:
        checkExtinguishAndChangeStatus(); // check some other agent already extinguish the fire
        if(AgentStatus.EXECUTING_TASKS != agentModel.getStatus()) {
          return;
        }
        executeTask();
        checkExtinguishAndChangeStatus(); // check the agent itself extinguish the fire
        break;
      case NECESSITY_OF_CHEMICAL:
        setupFireStationTarget();
        break;
      case TRAVELING_TO_FIRE_STATION:
        checkFireStationAndChangeStatus();
        if(AgentStatus.TRAVELING_TO_FIRE_STATION != agentModel.getStatus()) {
          return;
        }
        move();
        reachDestination();
        break;
      case FILLING_UP_CHEMICAL:
        fillingUpChemicals();
        break;
      default:
        break;
    }
  }

  private void executeTask() {
    extinguisher.extinguish(agentModel.getFire(), this);
  }

  private void checkExtinguishAndChangeStatus() {
    FireEntity fire = agentModel.getFire();
    double chemicalAmount = agentModel.getChemicalAmount();
    boolean isFireExtinguished = extinguisher.isFireExtinguised(fire);

    if(chemicalAmount > 0 && !isFireExtinguished) {
      return;
    }

    if(chemicalAmount <= 0) {
      setAgentFromETtoNC();
    }

    if(!isFireExtinguished) {
      return;
    }

    if(fire.isEnable()) {
      setFireDisable(fire);
    }

    setAgentFromETtoS();
  }

  private void searchFires() {
    if(targetLocation != null) {
      return;
    }
    
    if(waitAtIncident) {
      if(searchTimeAtIncident++ > MAX_SEARCH_TIME_AT_INCIDENT) {
        setAgentFromStoTFS();
      }
    }

    List<FireEntity> fires = firePersister.getCloseFires(agentModel.getX(), agentModel.getY(), agentModel.getVicinity());
    if(fires.isEmpty()) {
      return;
    }

    lookCloseFires(fires);
  }
 
  private void setupFireStationTarget() {
    // Need to find the closest fire station
    FireStationEntity fireStationEntity = fireStationPersister.getClosestFireStation(currentLocation.x, currentLocation.y, getVicinity());
    if(fireStationEntity == null) {
      LOGGER.error("Could not find a close Fire Station. Agent: "+this);
      return;
    }
    agentModel.setFireStation(fireStationEntity);
    // Need to get the road location for the fire station
    targetLocation = new MutableInt2D(fireStationEntity.getRoadX(), fireStationEntity.getRoadY());
    // Check whether the agent is so close-to-the-target or on-the-target
    AgentMove agentMove = getAgentMoveToTarget();
    if(agentMove != AgentMove.FAR_FROM_TARGET) {
      if(agentMove == AgentMove.CLOSE_TO_TARGET) {
        move();
      }
      setAgentFromNCtoFUC();
      return;
    }
    // setup the route by reusing the code
    routePlanner.createRouteToReachFireStation(this);
    setAgentFromNCtoTFS();
  }

  private void lookCloseFires(List<FireEntity> fires) {
    for(FireEntity fire : fires) {
      if(fire.getBurningForest() == null) {
        burn(fire);
      }
    }
    agentModel.setCloseFires(fires);

    setAgentFromStoFC();
  }
  
  private void burn(FireEntity fire) {
    ForestEntity burningForest = forestPersister.getForest(fire.getX(), fire.getY());
    if(burningForest == null) {
      LOGGER.error("Cannot find the forest id for a given fire. Agent: "+this+" fire: "+fire);
    }
    fire.setBurningForest(burningForest);
    burningForest.addFire(fire);
  }

  private void move() {
    Double2D currentPosition = positioningCanvas.getCurrentLocation(this.agentAvatar);
    if(currentPosition == null){
      LOGGER.error("agent's position is set to be null: "+this);
    }

    currentLocation.setTo(currentPosition);
    MutableDouble2D nextLocation = movementHandler.getNewLocation(this);

    if(nextLocation == null) {
      return;
    }

    newLocation.setTo((int)nextLocation.x, (int)nextLocation.y);

    boolean isValidMove = movementHandler.isValidMove(newLocation, velocity);
    if(!isValidMove) {
      return;
    }

    // Avoid two agents stay at the same position for two attempts
    boolean isAcceptablePosition = movementHandler.acceptablePosition(getId(), new Double2D(newLocation.x, newLocation.y));
    if(!isAcceptablePosition && acceptableAttempts < MAX_ACCEPTABLE_ATTEMPTS) {
      acceptableAttempts++;
      return;
    }

    if(acceptableAttempts > 0) {
      acceptableAttempts = 0;
    }

    agentModel.setX(newLocation.x);
    agentModel.setY(newLocation.y);
    positioningCanvas.setAgentLocation(this.agentAvatar);
  }

  private void checkFireAndChangeStatus() {
    FireEntity fire = agentModel.getFire();
    if(fire != null && !fire.isExtinguished()) {
      return;
    }
    setAgentFromTtoS();
    return;
  }

  private void reachDestination() {
    Route route = routePlanner.getRoute(this);
    boolean isReach = route.reachDestination(getX(), getY());
    if(isReach) {
      positionAgent(route);
      if(getStatus() == AgentStatus.TRAVELING) {
        setAgentFromTtoET();
      }
      else if(getStatus() == AgentStatus.TRAVELING_TO_FIRE_STATION) {
        setAgentFromNCtoFUC();
      }
    }
  }

  private void positionAgent(Route route) {
    Int2D position = null;
    position = movementHandler.getCloseTargetLocation(this, route);
    if(position == null) {
      return;
    }
    agentModel.setX(position.x);
    agentModel.setY(position.y);
    positioningCanvas.setAgentLocation(this.agentAvatar);
  }

  private void fillingUpChemicals() {
    double addedAgentChemicalAmt = 0.0;
    double reducedFireStationChemicalAmt = 0.0;
    double chemicalCoefficient = agentModel.getChemical().getExtinguishingCoefficient();
    FireStationEntity fireStation = agentModel.getFireStation();
    int fillupPressure = agentModel.getFillingUpPressure();
    double flowAmount = fillupPressure * chemicalCoefficient;
    double requiredAgentChemicalAmt = (agentModel.getInitialChemicalAmount() - agentModel.getChemicalAmount()) * chemicalCoefficient;

    if(requiredAgentChemicalAmt == 0) {
      LOGGER.info("Agent does not need chemicals. Agent Id: "+agentModel.getId());
      setAgentFromFUCtoS();
      return;
    }

    if(fireStation.getChemicalAmount() == 0) {
      LOGGER.info("Firestation has no chemical amount, some other agent empty the station. "
          + "Fire Station Id: "+fireStation.getId()+" Agent id: "+agentModel.getId());
      if(agentModel.getChemicalAmount() < (agentModel.getInitialChemicalAmount()/2)) {
        setAgentFromFUCtoS();
      }
      else {
        setAgentFromFUCtoNC();
      }
      return;
    }

    if(fireStation.getChemicalAmount() >= flowAmount 
        && requiredAgentChemicalAmt >= flowAmount) { // normal case
      reducedFireStationChemicalAmt = flowAmount;
      addedAgentChemicalAmt = fillupPressure;
    }
    else if(requiredAgentChemicalAmt < flowAmount) {// agent is going to filling up
      if(requiredAgentChemicalAmt < fireStation.getChemicalAmount()){ 
        reducedFireStationChemicalAmt = requiredAgentChemicalAmt;
        // this amount should equal to = requiredAgentChemicalAmt / chemicalCoefficient
        addedAgentChemicalAmt = agentModel.getInitialChemicalAmount() - agentModel.getChemicalAmount();
      }
      else { // fire station cannot provide the last drop
        reducedFireStationChemicalAmt = fireStation.getChemicalAmount();
        addedAgentChemicalAmt = fireStation.getChemicalAmount() / chemicalCoefficient;
      }
    }
    else if(fireStation.getChemicalAmount() < flowAmount) { // station has less chemical amount
      reducedFireStationChemicalAmt = fireStation.getChemicalAmount();
      addedAgentChemicalAmt = fireStation.getChemicalAmount() / chemicalCoefficient;
    }
    double agentNewChemicalAmt = agentModel.getChemicalAmount() + addedAgentChemicalAmt;
    double fireStationNewChemicalAmt = fireStation.getChemicalAmount() - reducedFireStationChemicalAmt;
    if(agentNewChemicalAmt > agentModel.getInitialChemicalAmount()) {
      LOGGER.error("Agent has more chemicals: agentNewChemicalAmt = "+agentNewChemicalAmt
        +" addedAgentChemicalAmt = "+addedAgentChemicalAmt+" reducedFireStationChemicalAmt = "+reducedFireStationChemicalAmt
        +" flowAmount = "+flowAmount+" requiredAgentChemicalAmt = "+requiredAgentChemicalAmt
        +" Agent = "+this+" Fire Station = "+fireStation);
    }
    if(fireStationNewChemicalAmt < 0) {
      LOGGER.error("Fire Station has negetive chemicals: agentNewChemicalAmt = "+agentNewChemicalAmt
        +" addedAgentChemicalAmt = "+addedAgentChemicalAmt+" reducedFireStationChemicalAmt = "+reducedFireStationChemicalAmt
        +" flowAmount = "+flowAmount+" requiredAgentChemicalAmt = "+requiredAgentChemicalAmt
        +" Agent = "+this+" Fire Station = "+fireStation);
    }
    agentModel.setChemicalAmount(agentNewChemicalAmt);
    fireStation.setChemicalAmount(fireStationNewChemicalAmt);

    if(fireStation.getChemicalAmount() == 0) { // Fire station has no more chemicals
      setFireStationOutOfService(fireStation);
      setAgentFromFUCtoS();
      return;
    }
    if(agentModel.getChemicalAmount() == agentModel.getInitialChemicalAmount()) { // agent filled up its tank
      setAgentFromFUCtoS();
    }
  }

  public void checkFireStationAndChangeStatus() {
    FireStationEntity fireStation = agentModel.getFireStation();
    if(fireStation.getChemicalAmount() > 0) {
      return;
    }
    setFireStationOutOfService(fireStation);

    if(agentModel.getChemicalAmount() < (agentModel.getInitialChemicalAmount()/2)) {
      setAgentFromTFStoS();
      return;
    }
    setAgentFromTFStoNC();
  }

  // ** Agents and Fire Stations change their status ** //

  private void setFireStationOutOfService(FireStationEntity fireStation) {
    fireStation.setOutOfService(true);
    fireStationCanvas.addLabel(new Int2D(fireStation.getStationX(), fireStation.getStationY()), OUT_OF_SERVICE);
    fireStationPersister.save(fireStation);
    LOGGER.info("Fire Station out of Service. Fire Station ID: "+fireStation.getId());
  }

  // An Agent search for a fire and find it, It cannot form a coalition, so it is going to travel to the 
  // fire
  private void setAgentFromStoT() {
    setStatus(AgentStatus.TRAVELING);
    LOGGER.info("Find a fire, start maneuvering. "
      + "S (Search) -> T (Travel). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // An agent is waiting for a fire, find a fire. The agent has ability to form a coalition
  // The agent start the process of forming a coalition
  private void setAgentFromStoFC() {
    setStatus(AgentStatus.FORMING_COALITIONS);
    LOGGER.info("Find a fire, start forming coalitions. "
      + "S (Search) -> FC (Forming Coalition). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // An agent fills up the chemical. It is now actively looking for a task to execute.
  private void setAgentFromFUCtoS() {
    setStatus(AgentStatus.SEARCHING);
    agentModel.setFireStation(null);
    LOGGER.info("Searching for fires. "
      + "FUC (Filling up chemical) -> S (Search). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // An agent was filling up its chemical. Fire Station cannot provide the chemical amout it needs
  // the agent needs to find another station to fill up its chemical amount required.
  private void setAgentFromFUCtoNC() {
    setStatus(AgentStatus.NECESSITY_OF_CHEMICAL);
    agentModel.setFireStation(null);
    LOGGER.info("Need to find another fire station to fill up chemicals. "
      + "FUC (Filling up Chemical) -> NC (Necessity of Chemical). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // Agent reach the fire station, so agent start filling up chemicals to its tank
  private void setAgentFromNCtoFUC() {
    setStatus(AgentStatus.FILLING_UP_CHEMICAL);
    targetLocation = null;
    LOGGER.info("Ready to fill up the tanks. "
      + "NC (Necessity of Chemical) -> FUC (Filling up chemical). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // Reach the fire to execute the task
  private void setAgentFromTtoET() {
    setStatus(AgentStatus.EXECUTING_TASKS);
    targetLocation = null;
    LOGGER.info("Ready to execute the task. "
      + "T (Travel) -> ET (Executing Task). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // Agent stop traveling because the fire got exhausted while the agent was traveling
  private void setAgentFromTtoS() {
    agentModel.setStatus(AgentStatus.SEARCHING);
    extinguisher.releaseAgent(this);
    routePlanner.releaseAgent(this);
    coalitionFormation.release(this);
    waitAtIncident = true;
    LOGGER.info("Agent stop moving because there is no fire any more. "
      + "T (Travel) -> S (Search). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // if an agent has no more chemicals to extinguish the fire
  private void setAgentFromETtoNC() {
    agentModel.setStatus(AgentStatus.NECESSITY_OF_CHEMICAL);
    extinguisher.releaseAgent(this);
    routePlanner.releaseAgent(this);
    coalitionFormation.release(this);
    LOGGER.info("Agent need more chemical to spare. "
      + "ET (Executing Task) -> NC (Necessity of Chemicals). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // The agent extinguish the fire, now wait at the buldling to find more fire to extinguish
  private void setAgentFromETtoS() {
    agentModel.setStatus(AgentStatus.SEARCHING);
    extinguisher.releaseAgent(this);
    routePlanner.releaseAgent(this);
    coalitionFormation.release(this);
    waitAtIncident = true;
    LOGGER.info("Agent has finish extinguish the fire. "
      + "ET (Executing Task) -> S (Search). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // The agent is which need more chemicals travel to the fire station
  private void setAgentFromNCtoTFS() {
    setStatus(AgentStatus.TRAVELING_TO_FIRE_STATION);
    LOGGER.info("Agent has started traveling to fire station. "
      + "NC (Necessity of Chemical) -> TFS (Travel Fire Station). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  // The agent stop travel to the fire station, because the fire station has no more chemical to serve
  private void setAgentFromTFStoNC() {
    setStatus(AgentStatus.NECESSITY_OF_CHEMICAL);
    routePlanner.releaseAgent(this);
    targetLocation = null;
    LOGGER.info("Agent stop moving to the Fire Station because fire station is empty. "
      + "TFS (Travel to Fire Station) -> NC (Necessity of Chemical). Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  private void setAgentFromTFStoS() {
    setStatus(AgentStatus.SEARCHING);
    routePlanner.releaseAgent(this);
    targetLocation = null;
    LOGGER.info("Agent stop moving to the Fire Station because fire station is empty. Agent has half tank of chemical. "
      + "TFS (Travel to Fire Station) -> S (Search). Agent id: "+getId()+" @ "+getCurrentLocation());
  }
  
  public void setAgentFromFCtoT() {
    setStatus(AgentStatus.TRAVELING);
    targetLocation = new MutableInt2D(agentModel.getFire().getX(),agentModel.getFire().getY());
    Route route = routePlanner.createRoute(this);
    if(route.getAgentMove() != AgentMove.FAR_FROM_TARGET) {
      if(route.getAgentMove() == AgentMove.CLOSE_TO_TARGET) {
        move();
      }
      positionAgent(route);
      setAgentFromTtoET();
      return;
    }
    LOGGER.info("Agent travel to the buring to forest after forming the coalition. "
        + "FC (Forming Coalition) -> T (Travel). Agent id: "+getId()+" @ "+getCurrentLocation());
  }
  
  public void setAgentFromFCtoS() {
    setStatus(AgentStatus.SEARCHING);
    agentModel.setCoalition(null);
    AgentEntity agentEntity = agentPersister.getAgent(getId());
    agentEntity.setCoalition(null);
    agentPersister.save(agentEntity);
    LOGGER.info("Agent start searching after attempting a coalition which failed. "
        + "FC (Forming Coalition) -> S (Searching). Agent id: "+getId()+" @ "+getCurrentLocation());
  }
  
  public void setAgentFromStoTFS() {
    setStatus(AgentStatus.TRAVELING_TO_FIRE_STATION);
    waitAtIncident = false;
    searchTimeAtIncident = 0;
    FireStationEntity fireStationEntity = agentModel.getFireStation();
    // Need to get the road location for the fire station
    targetLocation = new MutableInt2D(fireStationEntity.getRoadX(), fireStationEntity.getRoadY());
    // setup the route by reusing the code
    routePlanner.createRouteToReachFireStation(this);
    LOGGER.info("Agent wait at the burning forest searching for a fire. After "
        +MAX_SEARCH_TIME_AT_INCIDENT+" time, the agent decided to go back to the its own fire station to "
          + "refill its tank. S (Searching) -> TFS (Traveling to Fire Station). "
          + "Agent id: "+getId()+" @ "+getCurrentLocation());
  }

  private void setFireDisable(FireEntity fire) {
    fire.setEnable(false);
    LOGGER.info("Disable the fire. fire id: "+fire.getId());
  }
  
  public void setCoalition(AgentEntity agentEntity, CoalitionEntity coalitionEntity) {
    agentEntity.setCoalition(coalitionEntity);
    agentPersister.save(agentEntity);
    agentModel.setCoalition(coalitionEntity);
  }

  private AgentMove getAgentMoveToTarget() {
    double distance = currentLocation.distance(targetLocation);
    if(distance == 0.0) {
      return AgentMove.ON_TARGET;
    }
    if(distance <= agentModel.getSpeed()) {
      return AgentMove.CLOSE_TO_TARGET;
    }
    return AgentMove.FAR_FROM_TARGET;
  }

  public Integer getX(){
    return agentModel.getX();
  }

  public AgentPlayer withX(Integer x) {
    agentModel.setX(x);
    return this;
  }

  public Integer getY(){
    return agentModel.getY();
  }

  public AgentPlayer withY(Integer y) {
    agentModel.setY(y);
    return this;
  }

  public Color getColor(){
    return agentModel.getColor();
  }

  public Long getId(){
    return agentModel.getId();
  }

  public Long getVicinity() {
    return agentModel.getVicinity();
  }

  public MutableDouble2D getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(MutableDouble2D currentLocation) {
    this.currentLocation = currentLocation;
  }

  public AgentPlayer withCurrentLocation(MutableDouble2D currentLocation) {
    setCurrentLocation(currentLocation);
    return this;
  }

  public double getSpeed(){
    return agentModel.getSpeed();
  }

  public AgentPlayer withSpeed(double speed){
    agentModel.setSpeed(speed);
    return this;
  }

  public MutableInt2D getTargetLocation() {
    return targetLocation;
  }

  public void setTargetLocation(MutableInt2D targetLocation) {
    this.targetLocation = targetLocation;
  }

  public AgentPlayer withTargetLocation(MutableInt2D targetLocation) {
    setTargetLocation(targetLocation);
    return this;
  }

  public MutableDouble2D getVelocity() {
    return velocity;
  }

  public void setVelocity(MutableDouble2D velocity) {
    this.velocity = velocity;
  }

  public Network getMap() {
    return map;
  }

  public void setMap(Network map) {
    this.map = map;
  }

  public IntersectionEntity getTargetIntersection() {
    return targetIntersection;
  }

  public void setTargetIntersection(IntersectionEntity targetIntersection) {
    this.targetIntersection = targetIntersection;
  }

  public IntersectionEntity getSourceIntersection() {
    return sourceIntersection;
  }

  public void setSourceIntersection(IntersectionEntity sourceIntersection) {
    this.sourceIntersection = sourceIntersection;
  }

  public AgentStatus getStatus() {
    return agentModel.getStatus();
  }

  public void setStatus(AgentStatus agentStatus) {
    agentModel.setStatus(agentStatus);
  }

  public Chemical getChemical() {
    return agentModel.getChemical();
  }

  public void setChemical(Chemical chemical) {
    agentModel.setChemical(chemical);
  }

  public AgentPlayer withChemical(Chemical chemical) {
    setChemical(chemical);
    return this;
  }

  public Double getChemicalAmount() {
    return agentModel.getChemicalAmount();
  }

  public void setChemicalAmount(Double chemicalAmount) {
    agentModel.setChemicalAmount(chemicalAmount);
  }

  public AgentPlayer withChemicalAmount(Double chemicalAmount) {
    setChemicalAmount(chemicalAmount);
    return this;
  }

  public Long getMinimumFireProximity() {
    return agentModel.getMinimumFireProximity();
  }

  public void setMinimumFireProximity(Long minimumFireProximity) {
    agentModel.setMinimumFireProximity(minimumFireProximity);
  }

  public AgentPlayer withMinimumFireProximity(Long minimumFireProximity) {
    setMinimumFireProximity(minimumFireProximity);
    return this;
  }

  public MutableInt2D getWaterImpactCenter() {
    return agentModel.getWaterImpactCenter();
  }

  public void setWaterImpactCenter(MutableInt2D waterImpactCenter) {
    agentModel.setWaterImpactCenter(waterImpactCenter);
  }

  public AgentPlayer withWaterImpactCenter(MutableInt2D waterImpactCenter) {
    setWaterImpactCenter(waterImpactCenter);
    return this;
  }

  public int getWaterImpactRadius() {
    return agentModel.getWaterImpactRadius();
  }

  public void setWaterImpactRadius(int waterImpactRadius) {
    agentModel.setWaterImpactRadius(waterImpactRadius);
  }

  public AgentPlayer withWaterImpactRadius(int waterImpactRadius) {
    setWaterImpactRadius(waterImpactRadius);
    return this;
  }

  public Integer getSquirtPressure() {
    return agentModel.getSquirtPressure();
  }

  public AgentPlayer withSquirtPressure(Integer squirtPressure) {
    agentModel.setSquirtPressure(squirtPressure);
    return this;
  }
  
  public FireEntity getFire() {
    return agentModel.getFire();
  }

  public void setFire(FireEntity fire) {
    agentModel.setFire(fire);
  }

  public AgentModel getAgentModel() {
    return agentModel;
  }

  public void setAgentModel(AgentModel agentModel) {
    this.agentModel = agentModel;
  }

  public AgentPlayer withAgentModel(AgentModel agentModel) {
    setAgentModel(agentModel);
    return this;
  }

  public AgentAvatar getAgentAvatar() {
    return agentAvatar;
  }

  public void setAgentAvatar(AgentAvatar agentAvatar) {
    this.agentAvatar = agentAvatar;
  }

  @Override
  public boolean equals(Object other){
    return Pojomatic.equals(this, other);
  }

  @Override
  public String toString(){
    return Pojomatic.toString(this);
  }

  @Override
  public int hashCode(){
    return Pojomatic.hashCode(this);
  }

}
