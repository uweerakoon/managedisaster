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
public class BuildingPersisterTest {

  private static final String WALMART = "books";
  
  @Inject
  private ForestPersister buildingPersister;
  @Test
  public void testGetAllBuildings() {
    List<ForestEntity> buildingEntities = buildingPersister.getAllForests();
    assertTrue(!buildingEntities.isEmpty());
  }
  
  @Test
  public void testGetByName() {
    ForestEntity building = buildingPersister.getForest(WALMART);
    assertEquals(WALMART, building.getName());
  }

}
