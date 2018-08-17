package edu.usu.cs.mas.managedisaster.service.util;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;

public class MapperUtilTest {
  
  private MapperUtil mapperUtil = new MapperUtilImpl();

  @Test
  public void testGetAgentsFromAgentEntityList() {
    List<AgentEntity> agentEntities = new ArrayList<>();
    AgentEntity agentEntity = new AgentEntity().withX(20).withY(5)
        .withName("Unit Test Agent Entity")
        .withStatus(AgentStatus.SEARCHING).withRole(AgentRole.INDEPENDENT)
        .withColor(-16711936).withSpeed(34.6).withChemical("CARBONDIOXIDE");
    agentEntities.add(agentEntity);
    
    List<AgentModel> agents = mapperUtil.getAgentsFromAgentEntityList(agentEntities);
    
    AgentModel agent = agents.get(0);
    
    assertTrue(!agents.isEmpty());
    assertTrue(agent.getColor().equals(Color.GREEN));
    assertTrue(agent.getChemical().equals(Chemical.CARBONDIOXIDE));
  }

}
