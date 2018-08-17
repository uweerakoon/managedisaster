package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class AgentPersisterTest {
  
  @Inject
  private AgentPersister agentPersister;

  @Test
  public void testGetAllAgents() {
    List<AgentEntity> listAgentEntity = agentPersister.getAllActiveAgents();
    
    assertTrue(!listAgentEntity.isEmpty());
  }
  
  @Test
  public void testAgentChemical() {
    AgentEntity agent = agentPersister.getAgent(1L);
    assertEquals(Chemical.HALON, Chemical.valueOf(agent.getChemical()));
  }

}
