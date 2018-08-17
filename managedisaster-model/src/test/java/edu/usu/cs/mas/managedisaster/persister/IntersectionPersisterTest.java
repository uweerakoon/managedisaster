package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class IntersectionPersisterTest {
  
  private static final int NUMBER_INTERSECTIONS = 9;
  
  @Inject
  private IntersectionPersister intersectionPersister;

  @Test
  public void testGetAllIntersection() {
    List<IntersectionEntity> intersections = null;
    try{
      intersections = intersectionPersister.getAllIntersection();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    assertEquals(NUMBER_INTERSECTIONS, intersections.size());
  }
  
  @Test
  public void testGetEntity() {
    IntersectionEntity intersection = intersectionPersister.getIntersection(1L);
    assertNotNull(intersection);
  }

}
