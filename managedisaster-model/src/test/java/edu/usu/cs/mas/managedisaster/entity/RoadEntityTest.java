package edu.usu.cs.mas.managedisaster.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.common.RoadOrientation;

public class RoadEntityTest {

  @Test
  public void testContains() {
    RoadEntity road = new RoadEntity().withX(95).withY(55).withLength(40)
        .withWidth(5).withOrientation(RoadOrientation.VERTICAL);
    
    assertTrue(road.contains(new MutableInt2D(95,75)));
    assertFalse(road.contains(new MutableInt2D(50,50)));
    
    road = road.withX(0).withY(0).withOrientation(RoadOrientation.HORIZONTAL);
    
    assertTrue(road.contains(new MutableInt2D(39,0)));
    assertFalse(road.contains(new MutableInt2D(40,1)));
  }

}
