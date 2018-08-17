package edu.usu.cs.mas.managedisaster.canvas;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

import sim.field.grid.IntGrid2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;
import edu.usu.cs.mas.managedisaster.persister.RoadPersisterImpl;

public class RoadCanvasImplTest {

  private RoadCanvas roadCanvas;
  
  @Before
  public void setup(){
    roadCanvas = new RoadCanvasImpl(new RoadPersisterImpl(new HibernateUtilImpl()));
  }
  
//  @Test
  public void testDrawRoads() {
    roadCanvas.drawRoads();
    FastValueGridPortrayal2D roadPortrayal = roadCanvas.getRoadPortrayal();
    IntGrid2D roadGrid = (IntGrid2D) roadPortrayal.getField();
    assertEquals(1, roadGrid.field[0][0]);
    assertEquals(1, roadGrid.field[20][5]);
  }

}
