package edu.usu.cs.mas.managedisaster;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireStationCanvas;
import edu.usu.cs.mas.managedisaster.canvas.PositioningCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvas;
import edu.usu.cs.mas.managedisaster.chart.FireGridLineChart;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.collection.FireEngineCollection;
import edu.usu.cs.mas.managedisaster.collection.FireTruckCollection;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinter;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEngineEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.FireTruckEntity;
import edu.usu.cs.mas.managedisaster.handler.ChemicalRefiller;
import edu.usu.cs.mas.managedisaster.handler.CoalitionFormation;
import edu.usu.cs.mas.managedisaster.handler.CommunicationHandler;
import edu.usu.cs.mas.managedisaster.handler.DataCuller;
import edu.usu.cs.mas.managedisaster.handler.FireDiffuser;
import edu.usu.cs.mas.managedisaster.handler.TweetDispatcher;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.model.FireEngineModel;
import edu.usu.cs.mas.managedisaster.model.FireTruckModel;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentUtilityPersister;
import edu.usu.cs.mas.managedisaster.persister.FireEnginePersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.FireTruckPersister;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.player.FireEnginePlayer;
import edu.usu.cs.mas.managedisaster.player.FireTruckPlayer;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterSpringConfig;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtil;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;

public class Simulator extends SimState{

  private static final long serialVersionUID = 4556018316823608110L;
  private static final Logger LOGGER = Logger.getLogger(Simulator.class);

  public static final int GRID_WIDTH = 100;
  public static final int GRID_HEIGHT = 100;

  private static final int FIRE_SCHEDULE_ORDER = 1;
  private static final double FIRE_SCHEDULE_INTERVAL = 1;

  private static final int CHEMICAL_REFILLER_SCHEDULE_ORDER = 1;
  private static final double CHEMICAL_REFILLER_SCHEDULE_INTERVAL = 3;

  private static final int TWEET_SCHEDULE_ORDER = 1;
  private static final double TWEET_SCHEDULE_INTERVAL = 2;

  private static final int DATA_CULLER_ORDER = 1;
  private static final double DATA_CULLER_INTERVAL = 2;

  private static final int COALITION_FORMATION_ORDER = 1;
  private static final double COALITION_FORMATION_INTERVAL = 1;

  private FireCanvas fireCanvas;
  private PositioningCanvas positioningCanvas;
  private ForestCanvas forestCanvas;
  private RoadCanvas roadCanvas;
  private FireStationCanvas fireStationCanvas;

  private MapperUtil mapperUtil;
  private CommunicationHandler communicationHandler; 
  private FireDiffuser fireDiffuser;
  private ChemicalRefiller chemicalRefiller;
  private FireGridCsvPrinter fireGridCsvPrinter;
  private TweetDispatcher tweetDispatcher;
  private DataCuller dataCuller;
  private CoalitionFormation coalitionFormation;

  private AgentSociety agentSociety;
  private FireEngineCollection fireEngineCollection;
  private FireTruckCollection fireTruckCollection;

  private AgentPersister agentPersister;
  private FirePersister firePersister;
  private TweetPersister tweetPersister;
  private FireEnginePersister fireEnginePersister;
  private FireTruckPersister fireTruckPersister;
  private AgentUtilityPersister agentUtilityPersister;

  private ApplicationContext applicationContext;

  public IntGrid2D forestsGrid;
  public IntGrid2D roadGrid;
  public IntGrid2D fireStationGrid;
  public DoubleGrid2D currentFireGrid;
  public DoubleGrid2D newFireGrid;
  public DoubleGrid2D currentSmokeGrid;
  public DoubleGrid2D newSmokeGrid;

  public Simulator(long seed, boolean useDefaultApplicationContext){
    super(seed);
    if(useDefaultApplicationContext){
      applicationContext = new AnnotationConfigApplicationContext(
        ManageDisasterSpringConfig.class, ManageDisasterModelSpringConfig.class);
      setupBeans();
    }
  }

  public void initializeBeans(ApplicationContext applicationContext){
    this.applicationContext = applicationContext;
    setupBeans();
  }

  private void setupBeans() {
    agentSociety = applicationContext.getBean(AgentSociety.class);
    fireEngineCollection = applicationContext.getBean(FireEngineCollection.class);
    fireTruckCollection = applicationContext.getBean(FireTruckCollection.class);
    positioningCanvas = applicationContext.getBean(PositioningCanvas.class);
    forestCanvas = applicationContext.getBean(ForestCanvas.class);
    roadCanvas = applicationContext.getBean(RoadCanvas.class);
    fireStationCanvas = applicationContext.getBean(FireStationCanvas.class);
    agentPersister = applicationContext.getBean(AgentPersister.class);
    mapperUtil = applicationContext.getBean(MapperUtil.class);
    communicationHandler = applicationContext.getBean(CommunicationHandler.class);
    fireCanvas = applicationContext.getBean(FireCanvas.class);
    fireGridCsvPrinter = applicationContext.getBean(FireGridCsvPrinter.class);
    firePersister = applicationContext.getBean(FirePersister.class);
    fireDiffuser = applicationContext.getBean(FireDiffuser.class);
    chemicalRefiller = applicationContext.getBean(ChemicalRefiller.class);
    tweetPersister = applicationContext.getBean(TweetPersister.class);
    fireEnginePersister = applicationContext.getBean(FireEnginePersister.class);
    fireTruckPersister = applicationContext.getBean(FireTruckPersister.class);
    tweetDispatcher = applicationContext.getBean(TweetDispatcher.class);
    dataCuller = applicationContext.getBean(DataCuller.class);
    coalitionFormation = applicationContext.getBean(CoalitionFormation.class);
    agentUtilityPersister = applicationContext.getBean(AgentUtilityPersister.class);
  }

  @Override
  public void start(){
    super.start();

    positioningCanvas.clearPositions();

    addAgents();
    addFireEngines();
    addFireTrucks();

    communicationHandler.addAllToCommunicationNetwork();
    communicationHandler.addCommunicationLinks();

    forestsGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    forestCanvas.setForestsGrid(forestsGrid);
    forestCanvas.drawForests();

    roadGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    roadCanvas.setRoadGrid(roadGrid);
    roadCanvas.drawRoads();

    fireStationGrid = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
    fireStationCanvas.setFireStationGrid(fireStationGrid);
    fireStationCanvas.drawFireStations();

    currentFireGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT, 0);
    newFireGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT, 0);
    currentSmokeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT, 0);
    newSmokeGrid = new DoubleGrid2D(GRID_WIDTH, GRID_HEIGHT, 0);
    fireCanvas.setCurrentFireGrid(currentFireGrid);
    fireCanvas.setNewFireGrid(newFireGrid);
    fireCanvas.setCurrentSmokeGrid(currentSmokeGrid);
    fireCanvas.setNewSmokeGrid(newSmokeGrid);

    // Storing fire diffuser data in a csv file
    fireDiffuser.setStoreData(false);

    schedule.scheduleRepeating(Schedule.EPOCH, FIRE_SCHEDULE_ORDER, fireDiffuser,FIRE_SCHEDULE_INTERVAL);
    schedule.scheduleRepeating(Schedule.EPOCH, CHEMICAL_REFILLER_SCHEDULE_ORDER, chemicalRefiller, CHEMICAL_REFILLER_SCHEDULE_INTERVAL);
    schedule.scheduleRepeating(Schedule.EPOCH, TWEET_SCHEDULE_ORDER, tweetDispatcher, TWEET_SCHEDULE_INTERVAL);
    schedule.scheduleRepeating(Schedule.EPOCH, COALITION_FORMATION_ORDER, coalitionFormation, COALITION_FORMATION_INTERVAL);
    schedule.scheduleRepeating(Schedule.EPOCH,  DATA_CULLER_ORDER, dataCuller, DATA_CULLER_INTERVAL);

    fireGridCsvPrinter.init();
    
    // Setup the timers, when it is important
    firePersister.setSimState(this);
    tweetPersister.setSimState(this);
    agentUtilityPersister.setSimState(this);
  }

  private void addFireEngines() {
    List<FireEngineEntity> fireEngineEntities = fireEnginePersister.getAllActiveFireEngines();
    List<FireEngineModel> fireEngineModels = mapperUtil.getFireEngineModels(fireEngineEntities);
    for(FireEngineModel fireEngineModel: fireEngineModels) {
      FireEnginePlayer fireEnginePlayer = new FireEnginePlayer(fireEngineModel);
      fireEngineCollection.addFireEnginePlayer(fireEnginePlayer);
      positioningCanvas.setFireEngineLocation(fireEnginePlayer.getFireEngineAvatar());
      schedule.scheduleRepeating(fireEnginePlayer);
    }
  }

  private void addFireTrucks() {
    List<FireTruckEntity> fireTruckEntities = fireTruckPersister.getAllActiveFireTrucks();
    List<FireTruckModel> fireTruckModels = mapperUtil.getFireTruckModels(fireTruckEntities);
    for(FireTruckModel fireTruckModel : fireTruckModels) {
      FireTruckPlayer fireTruckPlayer = new FireTruckPlayer(fireTruckModel);
      fireTruckCollection.addFireTruckPlayer(fireTruckPlayer);
      positioningCanvas.setFireTruckLocation(fireTruckPlayer.getFireTruckAvatar());
      schedule.scheduleRepeating(fireTruckPlayer);
    }
  }

  private void addAgents() {
    List<AgentEntity> agentEntities = agentPersister.getAllActiveAgents();
    List<AgentModel> agentModels = mapperUtil.getAgentsFromAgentEntityList(agentEntities);
    for(AgentModel agentModel: agentModels){
      AgentPlayer agentPlayer = new AgentPlayer(agentModel,this.applicationContext);
      agentSociety.addAgentPlayer(agentPlayer);
      positioningCanvas.setAgentLocation(agentPlayer.getAgentAvatar());
      schedule.scheduleRepeating(agentPlayer);
    }
  }

  public static void main(String[] args){
    SimState state = new Simulator(System.currentTimeMillis(), true);
    double values[] = new double[]{0.1,0.5,0.7,0.9};
    for(int i = 0; i < values.length; i++) {
      for(int j=0; j < values.length; j++) {
        //    for(int i=0; i<values.length; i++) {
        double evap = values[i];
        double diffuse = values[j];
        FireEntity fire = ((Simulator)state).firePersister.getFire(1L);
        fire.setEvaporationRate(evap);
        fire.setDiffusionRate(diffuse);
        fire.setFireRadius(0);
        ((Simulator)state).firePersister.saveFire(fire);

        state.start();

        ((Simulator)state).fireGridCsvPrinter.init();

        do{
          if(!state.schedule.step(state)){
            break;
          }
        } while (state.schedule.getTime() < 5000);

        state.finish();
        if(LOGGER.isDebugEnabled()) {
          LOGGER.debug("Evaporation Rate: "+evap+" Diffusion Rate: "+diffuse);
        }

        ((Simulator)state).fireGridCsvPrinter.printRemaining();
        ((Simulator)state).fireGridCsvPrinter.closeFile();

        FireGridLineChart fireGridLineChart = new FireGridLineChart(
            "Iteration Vs Fire");
        fireGridLineChart.setImageFileName("FireGridStatus_evap_"+evap+"_diffuse_"+diffuse+".png");
        fireGridLineChart.createGraph("Iteration Index Vs Fire Amount");
        //    }
      }
    }

    LOGGER.info("End the simulation");
    System.exit(0);
  }

  public void runSimulation(SimState state) {

  }

  public PositioningCanvas getPositioningCanvas() {
    return positioningCanvas;
  }
}
