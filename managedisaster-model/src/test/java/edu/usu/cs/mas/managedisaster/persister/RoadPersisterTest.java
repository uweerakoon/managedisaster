package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class RoadPersisterTest {
  
  private static final Logger LOGGER = Logger.getLogger(RoadPersisterTest.class);
  
  private static final int NUMBER_ROADS = 12;
  
  @Inject
  private RoadPersister roadPersister;

  @Test
  public void testGetAllRoads() {
    List<RoadEntity> roadEntities = null;
    try{
      roadEntities = roadPersister.getAllRoads();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    
    assertEquals(NUMBER_ROADS, roadEntities.size());
  }

}
