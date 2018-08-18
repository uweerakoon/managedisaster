package edu.usu.cs.mas.managedisaster;

import static edu.usu.cs.mas.managedisaster.canvas.FireCanvasImpl.MAX_HEAT;

import java.awt.Color;

import javax.swing.JFrame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireStationCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvas;
import edu.usu.cs.mas.managedisaster.chart.Chart;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinter;
import edu.usu.cs.mas.managedisaster.display.TransportationLayout;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterSpringConfig;
import edu.usu.cs.mas.managedisaster.service.util.Cleaner;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.SimpleInspector;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.inspector.TabbedInspector;
import sim.util.gui.SimpleColorMap;

public class SimulatorGUI extends GUIState{
  
  private ApplicationContext applicationContext;
  
  private TransportationLayout transportationLayout;
  
  private ForestCanvas buildingCanvas;
  private RoadCanvas roadCanvas;
  private FireStationCanvas fireStationCanvas;
  private FireCanvas fireCanvas;
  private FireGridCsvPrinter fireGridCsvPrinter;
  private HibernateUtil hibernateUtil;
  private Cleaner cleaner;
  private Chart chart;
  
  public Display2D display;
  public JFrame displayFrame;
  
  FastValueLabeledGridPortrayal2D buildingsPortrayal = new FastValueLabeledGridPortrayal2D("Building Portrayal", true);
  FastValueGridPortrayal2D roadPortrayal = new FastValueGridPortrayal2D("Road Portrayal");
  FastValueLabeledGridPortrayal2D fireStationPortrayal = new FastValueLabeledGridPortrayal2D("Fire Station Portrayal");
  FastValueGridPortrayal2D firePortrayal = new FastValueGridPortrayal2D("Fire Portrayal");
  FastValueGridPortrayal2D smokePortrayal = new FastValueGridPortrayal2D("Smoke Portrayal");
  
  Color ash = new Color(189,189,189,255);
  Color brown = new Color(128,64,64,255);
  //TODO - RGB for white is 255,255,255; but once we change the color plate we cannot see any building or roads
  Color white = new Color(0,0,0,0);
  Color black = Color.black;
  Color red = Color.red;
  
  public SimulatorGUI(){
    super(new Simulator(System.currentTimeMillis(), false));
    
    applicationContext = new AnnotationConfigApplicationContext
        (ManageDisasterSpringConfig.class, ManageDisasterModelSpringConfig.class);

    setupBeans();
    ((Simulator) state).initializeBeans(applicationContext);
  }
  
  private void setupBeans() {
  	transportationLayout = applicationContext.getBean(TransportationLayout.class);
    buildingCanvas = applicationContext.getBean(ForestCanvas.class);
    roadCanvas = applicationContext.getBean(RoadCanvas.class);
    fireStationCanvas = applicationContext.getBean(FireStationCanvas.class);
    fireCanvas = applicationContext.getBean(FireCanvas.class);
    fireGridCsvPrinter = applicationContext.getBean(FireGridCsvPrinter.class);
    hibernateUtil = applicationContext.getBean(HibernateUtil.class);
    cleaner = applicationContext.getBean(Cleaner.class);
    chart = applicationContext.getBean(Chart.class);
  }
  
  public SimulatorGUI(SimState simState){
    super(simState);
  }
  
  public static void main(String[] arg){
    new SimulatorGUI().createController();
  }
  
  public static String getName(){
    return "Effort to Manage Disasters Intelligently";
  }
  
  @Override
  public void start(){
    super.start();
    setUpPortrayals();
  }
  
  @Override
  public void load(SimState state){
    super.load(state);
    setUpPortrayals();
  }
  
  public void setUpPortrayals(){
    Simulator simulator = (Simulator) state;
    transportationLayout.setPositioningCanvas(simulator.getPositioningCanvas().getPositions());
    
    buildingsPortrayal.setField(simulator.buildingsGrid);
    buildingsPortrayal.setMap(new SimpleColorMap(0, 1, white, brown));
    
    roadPortrayal.setField(simulator.roadGrid);
    roadPortrayal.setMap(new SimpleColorMap(0, 1, white, ash));
    
    fireStationPortrayal.setField(simulator.fireStationGrid);
    fireStationPortrayal.setMap(new SimpleColorMap(0, 1, white, Color.green.darker()));
    
    firePortrayal.setField(simulator.currentFireGrid);
    firePortrayal.setMap(new SimpleColorMap(0, MAX_HEAT, new Color(255, 255, 255, 0), red));
    smokePortrayal.setField(simulator.currentSmokeGrid);
    smokePortrayal.setMap(new SimpleColorMap(5, 500, new Color(255, 255, 255, 0), Color.BLACK));
    
    buildingCanvas.setForestsPortrayal(buildingsPortrayal);
    roadCanvas.setRoadPortrayal(roadPortrayal);
    fireStationCanvas.setFireStationPortrayal(fireStationPortrayal);
    fireCanvas.setFirePortrayal(firePortrayal);
    fireCanvas.setSmokePortrayal(smokePortrayal);
    
    display.reset();
    display.repaint();
    
  }
  
  @Override
  public void init(Controller controller){
    super.init(controller);
    
    display = new Display2D(600, 600, this);
    displayFrame = display.createFrame();
    controller.registerFrame(displayFrame);
    displayFrame.setVisible(true);
    displayFrame.setTitle("Disaster Management System - Utah State University");
    
    display.attach(buildingsPortrayal, "Buildings");
    display.attach(roadPortrayal, "Roads");
    display.attach(fireStationPortrayal, "Fire Stations");
    display.attach(smokePortrayal, "Smoke");
    display.attach(firePortrayal, "Fire");
    display.attach(transportationLayout.getTransporationLayout(), transportationLayout.getName());
    
    // specify the backdrop color  -- what gets painted behind the displays
    display.setBackdrop(Color.white);
    
    cleaner.initialize();
  }
  
  @Override
  public void quit(){
    super.quit();
    chart.plotCharts();
    fireGridCsvPrinter.printRemaining();
    fireGridCsvPrinter.closeFile();
    if(displayFrame != null){
      displayFrame.dispose();
    }
    displayFrame = null;
    display = null;
    cleaner.cleanup();
    hibernateUtil.shutdown();
    // TODO - need to clean the database connections
  }
  
  @Override
  public Inspector getInspector() {
    TabbedInspector tabbedInspector = new TabbedInspector(false); //not volatile
    tabbedInspector.setUpdatingAllInspectors(false); //lazily update the inspectors
    Inspector simulatorInspector = new SimpleInspector(state, this);
    tabbedInspector.addInspector(simulatorInspector, "Simulator Properties");
    return tabbedInspector;
  }

}
