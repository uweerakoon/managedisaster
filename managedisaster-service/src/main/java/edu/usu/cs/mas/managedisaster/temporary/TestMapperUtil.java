package edu.usu.cs.mas.managedisaster.temporary;

import java.util.ArrayList;
import java.util.List;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtil;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtilImpl;

public class TestMapperUtil {
  
//  private MapperUtil mapperUtil = new MapperUtilImpl();

  public static void main(String[] agr) {
    MapperUtil mapperUtil = new MapperUtilImpl();
    List<AgentEntity> agentEntities = new ArrayList<>();
    AgentEntity agentEntity = new AgentEntity().withX(20).withY(5)
        .withName("Unit Test Agent Entity")
        .withStatus(AgentStatus.SEARCHING).withRole(AgentRole.INDEPENDENT)
        .withColor(-16711936).withSpeed(34.6);
    agentEntities.add(agentEntity);
    
    List<AgentModel> agents = mapperUtil.getAgentsFromAgentEntityList(agentEntities);
    
  }

}
