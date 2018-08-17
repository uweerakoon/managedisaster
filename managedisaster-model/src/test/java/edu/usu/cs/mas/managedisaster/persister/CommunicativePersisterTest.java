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

import edu.usu.cs.mas.managedisaster.entity.CommunicativeEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class CommunicativePersisterTest {

  private static final Logger LOGGER = Logger.getLogger(CommunicativePersisterTest.class);
  
  @Inject
  private CommunicativePersister communicativePersister;
  
  @Test
  public void testGetAllCommunicatives() {
    List<CommunicativeEntity> communicativeEntities = null;
    try{
      communicativeEntities = communicativePersister.getAllCommunicatives();
    }
    catch(Exception e){
      e.printStackTrace();
    }
    
    for(CommunicativeEntity entity : communicativeEntities){
      LOGGER.info("Entity: "+entity);
    }
  }

}
