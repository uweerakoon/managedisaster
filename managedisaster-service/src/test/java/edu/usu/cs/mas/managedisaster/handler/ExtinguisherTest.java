package edu.usu.cs.mas.managedisaster.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.util.TestUtil;

public class ExtinguisherTest extends TestUtil{
  private static final int ZERO_AMOUNT = 0;
  private static final double REDUCED_FIRE_VALUE = 0.02;
  private static final double THRESHOLD = 0.001;
  private static final int INIT_VALUE = 1;
  private static final int STARTING_RADIUS = 2;
  private static final int MAX_WATER_AMOUNT = 50;
  private static final double AVG_WATER_AMOUNT = 8.8;
  private static final MutableInt2D FIRE_COORDINATE = new MutableInt2D(1,1);
  private static final MutableInt2D AGENT_COORDINATE = new MutableInt2D(0,1);
  private static final MutableInt2D LONG_FIRE_COORDINATE = new MutableInt2D(4,4);
  private static final MutableInt2D LONG_WATER_CENTER = new MutableInt2D(2,2);
  private static final int SQUIRT_PRESSURE = 10;
  private static final double LONG_CHEMICAL_AMT = 49.95;
  
  private static final int INIT_BUIDING_SIZE = 1, INIT_BUILDING_NO_POINTS = 1, INIT_BUIDLING_X = 1, INIT_BUILDING_Y = 1;
  
  private static final int W = 3;
  private static final int L = 3;
  private static final int LW = 5;
  private static final int LL = 5;
  
  private DoubleGrid2D currentWaterGrid = new DoubleGrid2D(W,L,0);
  private DoubleGrid2D newWaterGrid = new DoubleGrid2D(W,L, 0);
  
  private DoubleGrid2D currentFireGrid = new DoubleGrid2D(W,L,0);
  private DoubleGrid2D newFireGrid = new DoubleGrid2D(W,L, 0);
  
  private DoubleGrid2D currentSmokeGrid = new DoubleGrid2D(W,L,0);
  private DoubleGrid2D newSmokeGrid = new DoubleGrid2D(W,L, 0);
  
  private IntGrid2D buildingGrid = new IntGrid2D(W,L,0);
  
  @Mock
  private FireCanvas fireCanvas;
  @Mock
  private ForestCanvas buildingCanvas;
  
  private ExtinguisherImpl extinguisher;
  
  @Before
  public void setup() {
    initMocks(this);
    extinguisher = new ExtinguisherImpl(fireCanvas, buildingCanvas, currentWaterGrid, newWaterGrid);
    when(fireCanvas.getCurrentFireGrid()).thenReturn(currentFireGrid);
    when(fireCanvas.getNewFireGrid()).thenReturn(newFireGrid);
    when(fireCanvas.getCurrentSmokeGrid()).thenReturn(currentSmokeGrid);
    when(fireCanvas.getNewSmokeGrid()).thenReturn(newSmokeGrid);
    when(buildingCanvas.getForestsGrid()).thenReturn(buildingGrid);
  }

  @Test
  public void testInitiateValueMaximumChemical() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    double initChemicalAmt = agent.getChemicalAmount();
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);

    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    
    extinguisher.extinguish(fire, agent);
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(Chemical.WATER.getMaximumAmount(), currentWaterGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
  }
  
  @Test
  public void testInitiateValueMaximumFire() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    double initChemicalAmt = agent.getChemicalAmount();
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);

    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = 
        agent.getSquirtPressure() 
        * (1 - Chemical.WATER.getEvaporationRate())
        * Chemical.WATER.getExtinguishingCoefficient();
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    
    extinguisher.extinguish(fire, agent);
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentWaterGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
  }
  
  @Test
  public void testInitiateValueMaximumSmoke() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    double initChemicalAmt = agent.getChemicalAmount();
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);

    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = 
        agent.getSquirtPressure() 
        * (1 - Chemical.WATER.getEvaporationRate())
        * Chemical.WATER.getExtinguishingCoefficient() - INIT_VALUE;
    
    extinguisher.extinguish(fire, agent);
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentWaterGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
  }
  
  @Test
  public void testShortVicinity() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE)
        .withMinimumFireProximity(3L);
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(LONG_FIRE_COORDINATE.x).withY(LONG_FIRE_COORDINATE.y);
    
    setupLongGrids();
    
    buildingGrid.field[LONG_FIRE_COORDINATE.x][LONG_FIRE_COORDINATE.y] = INIT_VALUE;
    buildingGrid.field[LONG_FIRE_COORDINATE.x - 2][LONG_FIRE_COORDINATE.y - 2] = INIT_VALUE;
    currentFireGrid.field[LONG_FIRE_COORDINATE.x][LONG_FIRE_COORDINATE.y] = INIT_VALUE;
    currentSmokeGrid.field[LONG_FIRE_COORDINATE.x][LONG_FIRE_COORDINATE.y] = INIT_VALUE;
    
    extinguisher.extinguish(fire, agent);
    
    assertEquals(LONG_WATER_CENTER, agent.getWaterImpactCenter());
    assertEquals(LONG_CHEMICAL_AMT, agent.getChemicalAmount(), THRESHOLD);
    
    assertEquals(INIT_VALUE, currentFireGrid.field[LONG_FIRE_COORDINATE.x][LONG_FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[LONG_FIRE_COORDINATE.x][LONG_FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(MAX_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNoChemicalAmountTest() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withChemicalAmount(0.0);
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);
    extinguisher.extinguish(fire, agent);
  }
  
  @Test(expected = NullPointerException.class)
  public void testNoChemicalTest() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withChemical(null);
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);
    extinguisher.extinguish(fire, agent);
  }
  
  @Test(expected = NullPointerException.class)
  public void testNoBuildingTest() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withChemical(null);
    FireEntity fire = getFire();
    extinguisher.extinguish(fire, agent);
  }
  
  @Test
  public void testExtinguish1Iteration() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    double initChemicalAmt = agent.getChemicalAmount();
    FireEntity fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);

    agent.withWaterImpactCenter(FIRE_COORDINATE).withWaterImpactRadius(STARTING_RADIUS);
    
    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentWaterGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    
    extinguisher.extinguish(fire, agent);
    
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
    assertEquals(STARTING_RADIUS + 1, agent.getWaterImpactRadius(), THRESHOLD);
    assertEquals(REDUCED_FIRE_VALUE, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(INIT_VALUE, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
  }
  
  @Test
  public void testExtinguish2Iterations() {
    AgentPlayer agent = getAgent(null).withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y + 1).withSquirtPressure(SQUIRT_PRESSURE);
    double initChemicalAmt = agent.getChemicalAmount();
    FireEntity fire = createFire(3, 9, 1, 1).withX(LONG_FIRE_COORDINATE.x - 2).withY(LONG_FIRE_COORDINATE.y - 2);

    setupLongGrids();
    setupGrids();
    
    extinguisher.extinguish(fire, agent);
    
    assertInitialAgentGrids(agent, initChemicalAmt);
    
    extinguisher.extinguish(fire, agent);
    
    assertAgentAndGrids(agent, initChemicalAmt);
  }

  private void assertInitialAgentGrids(AgentPlayer agent, double initChemicalAmt) {
    assertEquals(INIT_VALUE, agent.getWaterImpactRadius(), THRESHOLD);
    assertEquals(LONG_WATER_CENTER, agent.getWaterImpactCenter());
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[agent.getWaterImpactCenter().x - 1][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y - 1], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y + 1], THRESHOLD);
    assertEquals(INIT_VALUE, currentSmokeGrid.field[agent.getWaterImpactCenter().x + 1][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(MAX_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
  }

  private void assertAgentAndGrids(AgentPlayer agent, double initChemicalAmt) {
    assertTrue(agent.getChemicalAmount() < initChemicalAmt);
    assertEquals(STARTING_RADIUS, agent.getWaterImpactRadius(), THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x - 1][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y - 1], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y + 1], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x + 1][FIRE_COORDINATE.y], THRESHOLD);
    
    assertEquals(AVG_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x - 1][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(AVG_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y - 1], THRESHOLD);
    assertEquals(MAX_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y], THRESHOLD);
    assertEquals(AVG_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y + 1], THRESHOLD);
    assertEquals(AVG_WATER_AMOUNT, currentWaterGrid.field[agent.getWaterImpactCenter().x + 1][agent.getWaterImpactCenter().y], THRESHOLD);
  }
  
  private void setupGrids() {
    for(int i = INIT_VALUE; i < 4; i++) {
      for(int j = INIT_VALUE; j < 4; j++) {
        buildingGrid.field[i][j] = INIT_VALUE;
      }
    }
    
    currentSmokeGrid.field[1][2] = INIT_VALUE;
    currentSmokeGrid.field[2][1] = INIT_VALUE;
    currentSmokeGrid.field[2][2] = INIT_VALUE;
    currentSmokeGrid.field[2][3] = INIT_VALUE;
    currentSmokeGrid.field[3][2] = INIT_VALUE;
    
    currentFireGrid.field[2][2] = INIT_VALUE;
  }
  
  private void setupLongGrids() {
    currentWaterGrid = new DoubleGrid2D(LW,LL,0);
    newWaterGrid = new DoubleGrid2D(LW,LL, 0);
    
    currentFireGrid = new DoubleGrid2D(LW,LL,0);
    newFireGrid = new DoubleGrid2D(LW,LL, 0);
    
    currentSmokeGrid = new DoubleGrid2D(LW,LL,0);
    newSmokeGrid = new DoubleGrid2D(LW,LL, 0);
    
    buildingGrid = new IntGrid2D(LW,LL,0);
    
    when(fireCanvas.getCurrentFireGrid()).thenReturn(currentFireGrid);
    when(fireCanvas.getNewFireGrid()).thenReturn(newFireGrid);
    when(fireCanvas.getCurrentSmokeGrid()).thenReturn(currentSmokeGrid);
    when(fireCanvas.getNewSmokeGrid()).thenReturn(newSmokeGrid);
    when(buildingCanvas.getForestsGrid()).thenReturn(buildingGrid);
    
    extinguisher.setCurrentWaterGrid(currentWaterGrid);
    extinguisher.setNewWaterGrid(newWaterGrid);
  }

}
