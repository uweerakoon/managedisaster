package edu.usu.cs.mas.managedisaster.handler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterSpringConfig;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {ManageDisasterModelSpringConfig.class, ManageDisasterSpringConfig.class}, 
        loader=AnnotationConfigContextLoader.class)
public class CoalitionFormationTest {
	
	@Inject
  private AgentPersister agentPersister;
	@Inject
	private MapperUtil mapperUtil;
	@Inject
	private AgentSociety agentSociety;
	
	private CoalitionFormationImpl coalitionFormationImpl;
	
	@Before
	public void setup() {
		coalitionFormationImpl = new CoalitionFormationImpl();
	}
	
	@Ignore
	@Test
	public void test() {
		AgentEntity agent1 = agentPersister.getAgent(1L);
		AgentEntity agent2 = agentPersister.getAgent(2L);
		List<AgentEntity> agentEntities = Arrays.asList(agent1, agent2);
		List<AgentModel> agentModels = mapperUtil.getAgentsFromAgentEntityList(agentEntities);
		List<AgentPlayer> agentPlayers = new ArrayList<AgentPlayer>();
		for(AgentModel agentModel: agentModels){
      AgentPlayer agentPlayer = new AgentPlayer(agentModel, null/*movementHandler*/, null/*positioningCanvas*/, null/*firePersister*/, 
          null/*routePlanner*/, null/*extinguisher*/, null/*buildingPersister*/, null/*buildingCanvas*/);
      agentSociety.addAgentPlayer(agentPlayer);
      agentPlayers.add(agentPlayer);
		}
		coalitionFormationImpl.initiateCoalitions(agentPlayers);
//		fail("Not yet implemented");
	}

}
