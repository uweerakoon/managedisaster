package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class CoalitionPersisterTest {

	@Inject
	private CoalitionPersister coalitionPersister;
	
	@Test
	public void testGetAllCoalitions() {
		List<CoalitionEntity> coalitions = coalitionPersister.getAllCoalitions();
//		assertTrue(!coalitions.isEmpty());
		/*AgentEntity agent = coalitions.get(0).getAgents().get(0);
		BuildingEntity building = coalitions.get(0).getBuringBuildings().get(0);
		System.out.println(coalitions);
		System.out.println(agent);
		System.out.println(building);*/
	}
}
