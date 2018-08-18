package edu.usu.cs.mas.managedisaster.service.planner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import sim.field.grid.IntGrid2D;
import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvasImpl;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;
import edu.usu.cs.mas.managedisaster.persister.IntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.IntersectionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.RoadIntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadIntersectionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.RoadPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadPersisterImpl;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.util.Route;
import edu.usu.cs.mas.managedisaster.service.util.TestUtil;

public class RoutePlannerTest extends TestUtil{

  private static final Logger LOGGER = Logger.getLogger(RoutePlannerTest.class);
  
  private static IntersectionPersister intersectionPersister;
  private static RoutePlannerImpl routePlanner;
  private static IntGrid2D roadGrid = new IntGrid2D(100, 100);
  
  @Mock
  private static ForestCanvas buildingCanvas;
  
  @BeforeClass
  public static void setup(){
    HibernateUtil hibernateUtil = new HibernateUtilImpl();
    RoadIntersectionPersister roadIntersectionPersister = new RoadIntersectionPersisterImpl(hibernateUtil);
    intersectionPersister = new IntersectionPersisterImpl(hibernateUtil);
    RoadPersister roadPersister = new RoadPersisterImpl(hibernateUtil);
    RoadCanvas roadCanvas = new RoadCanvasImpl(roadPersister);
    roadCanvas.setRoadGrid(roadGrid);
    roadCanvas.drawRoads();
    routePlanner = new RoutePlannerImpl(roadIntersectionPersister, intersectionPersister, roadPersister, roadCanvas, buildingCanvas);
  }
  
  @Test
  public void testGetNullRouteForAgent() {
    AgentPlayer agent = getAgent(null);
    Route route = routePlanner.getRouteForAgent(agent, null, null);
    assertNull(route);
  }
  
  @Test
  public void testCreateRoute() {
    AgentPlayer agent = getAgent(null);
    FireEntity fire = getFire();
    ForestEntity building = getBuilding();
    fire.setBurningForest(building);
    agent.getAgentModel().setFire(fire);
    roadGrid.field[50][50] = 1;
    Route route = routePlanner.createRoute(agent);
    assertNotNull(route);
    assertEquals(350, route.getExpectedArrivalTime());
    assertEquals(50,route.getDestination().getX().intValue());
    assertEquals(50,route.getDestination().getY().intValue());
    assertEquals(0,route.getSource().x);
    assertEquals(0,route.getSource().y);
  }
  
//  @Test
  public void testHorizontalUp() { // road orientation is horizontal, fire location is up side of the road
    AgentPlayer agent = getAgent(null);
    agent.setTargetLocation(new MutableInt2D(70, 35));
    Route route = routePlanner.createRoute(agent);
  }

}
