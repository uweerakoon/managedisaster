package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.entity.RoadIntersectionEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class RoadIntersectionPersisterTest {

  private static final int NUMBER_ROADINTERSECTIONS = 12;
  
  @Inject
  private RoadIntersectionPersister roadIntersectionPersister;
  
  @Test
  public void testGetAllRoadIntersections() {
    List<RoadIntersectionEntity> roadIntersections = null;
    try{
      roadIntersections = roadIntersectionPersister.getAllRoadIntersections();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    
    assertEquals(NUMBER_ROADINTERSECTIONS, roadIntersections.size());
  }
  
}
