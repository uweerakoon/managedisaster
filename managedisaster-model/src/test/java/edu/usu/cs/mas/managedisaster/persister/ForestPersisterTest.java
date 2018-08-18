package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class ForestPersisterTest {

  private static final String PINES = "pines";
  
  @Inject
  private ForestPersister forestPersister;
  @Test
  public void testGetAllForests() {
    List<ForestEntity> forestEntities = forestPersister.getAllForests();
    assertTrue(!forestEntities.isEmpty());
  }
  
  @Test
  public void testGetByName() {
    ForestEntity forest = forestPersister.getForest(PINES);
    assertEquals(PINES, forest.getName());
  }

}
