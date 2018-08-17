package edu.usu.cs.mas.managedisaster.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import edu.usu.cs.mas.managedisaster.Simulator;
import edu.usu.cs.mas.managedisaster.canvas.BuildingCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.handler.ExtinguisherImpl;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;
import edu.usu.cs.mas.managedisaster.service.util.TestUtil;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.util.MutableInt2D;

public class AgentPlayerTest extends TestUtil {
  
  private static final int W = 5;
  private static final int L = 5;
  private static final int INIT_RADIUS = -1;
  private static final int ZERO_AMOUNT = 0;
  private static final double THRESHOLD = 0.001;
  private static final int INIT_VALUE = 1;
  private static final int SQUIRT_PRESSURE = 10;
  private static final int HIGH_VALUE = 1001;
  
  private static final MutableInt2D FIRE_COORDINATE = new MutableInt2D(1,1);
  private static final MutableInt2D AGENT_COORDINATE = new MutableInt2D(0,1);
  private static final int INIT_BUIDING_SIZE = 1, INIT_BUILDING_NO_POINTS = 1, INIT_BUIDLING_X = 1, INIT_BUILDING_Y = 1;
  
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
  private BuildingCanvas buildingCanvas;
  @Mock
  private TweetPersister tweetPersister;
  @Mock
  private Simulator simulator;
  @Mock
  private FirePersister firePersister;
  @Mock
  private BuildingPersister buildingPersister;

  private ExtinguisherImpl extinguisher;
  private AgentModel agentModel;
  private AgentPlayer agentPlayer;
  private FireEntity fire;
  
  @Before
  public void setup() {
    initMocks(this);
    extinguisher = new ExtinguisherImpl(fireCanvas, buildingCanvas, currentWaterGrid, newWaterGrid);
    when(fireCanvas.getCurrentFireGrid()).thenReturn(currentFireGrid);
    when(fireCanvas.getNewFireGrid()).thenReturn(newFireGrid);
    when(fireCanvas.getCurrentSmokeGrid()).thenReturn(currentSmokeGrid);
    when(fireCanvas.getNewSmokeGrid()).thenReturn(newSmokeGrid);
    when(buildingCanvas.getBuildingsGrid()).thenReturn(buildingGrid);
    when(fireCanvas.isBuildingBurning(any(BuildingEntity.class))).thenReturn(true);
  }

  @Ignore // Need to set this up once agent player is coded correctly
  @Test
  public void testAgentExtinguishFireAndMoveToSearch() {
    agentModel = new AgentModel().withStatus(AgentStatus.EXECUTING_TASKS);
    fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);
    agentModel.withFire(fire);
    agentPlayer = new AgentPlayer(agentModel, null/*movementHandler*/, null/*positioningCanvas*/, null/*firePersister*/, 
                    null/*routePlanner*/, extinguisher, buildingPersister, buildingCanvas);
    agentPlayer.withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    agentPlayer = getAgent(agentPlayer);
    double initChemicalAmt = agentPlayer.getChemicalAmount();
    
    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    
    agentPlayer.step(null);
    
    assertEquals(ZERO_AMOUNT, currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(ZERO_AMOUNT, currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertEquals(Chemical.WATER.getMaximumAmount(), currentWaterGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y], THRESHOLD);
    assertTrue(agentPlayer.getChemicalAmount() < initChemicalAmt);
    
    // done working with fire
    when(fireCanvas.isBuildingBurning(any(BuildingEntity.class))).thenReturn(false);
    agentPlayer.step(simulator);
    
    assertNull(agentModel.getFire());
    assertNull(agentPlayer.getWaterImpactCenter());
    assertEquals(INIT_RADIUS, agentPlayer.getWaterImpactRadius(), THRESHOLD);
    assertEquals(false, fire.isEnable());
    assertEquals(AgentStatus.SEARCHING, agentModel.getStatus());
  }
  
  @Ignore // Need to set this up once agent player is coded correctly
  @Test
  public void testExhaustedAgentAndMoveToGetMoreChemical() {
    agentModel = new AgentModel().withStatus(AgentStatus.EXECUTING_TASKS);
    fire = createFire(INIT_BUIDING_SIZE, INIT_BUILDING_NO_POINTS, INIT_BUIDLING_X, INIT_BUILDING_Y).withX(FIRE_COORDINATE.x).withY(FIRE_COORDINATE.y);
    agentModel.withFire(fire);
    agentPlayer = new AgentPlayer(agentModel, null/*movementHandler*/, null/*positioningCanvas*/, null/*firePersister*/, 
                    null/*routePlanner*/, extinguisher, buildingPersister, buildingCanvas);
    agentPlayer.withX(AGENT_COORDINATE.x).withY(AGENT_COORDINATE.y).withSquirtPressure(SQUIRT_PRESSURE);
    agentPlayer = getAgent(agentPlayer);
    agentPlayer.setChemicalAmount((double)INIT_VALUE);
    
    buildingGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = INIT_VALUE;
    currentFireGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = HIGH_VALUE;
    currentSmokeGrid.field[FIRE_COORDINATE.x][FIRE_COORDINATE.y] = HIGH_VALUE;
    
    agentPlayer.step(null);
    
    assertNull(agentModel.getFire());
    assertNull(agentPlayer.getWaterImpactCenter());
    assertEquals(INIT_RADIUS, agentPlayer.getWaterImpactRadius(), THRESHOLD);
    assertEquals(false, fire.isEnable());
    assertEquals(AgentStatus.FILLING_UP_CHEMICAL, agentModel.getStatus());
    
  }

}
