package edu.usu.cs.mas.managedisaster.model.util;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ManageDisasterModelSpringConfig.class, 
        loader=AnnotationConfigContextLoader.class)
public class HibernateUtilTest extends TestCase {
  
  private static final Logger LOGGER = Logger.getLogger(HibernateUtilTest.class);
  
  @Inject
  private HibernateUtil hibernateUtil ;
  
  private EntityManager entityManager;
  
  @SuppressWarnings("unchecked")
  @Test
  public void testSaveAgentEntity(){
  	entityManager = hibernateUtil.getEntityManager();
    AgentEntity agentEntity = new AgentEntity().withX(20).withY(5)
        .withName("Unit Test Agent Entity")
        .withStatus(AgentStatus.SEARCHING).withRole(AgentRole.INDEPENDENT)
        .withColor(-16711936).withSpeed(34.6).withChemical("CABONDIOXIDE")
        .withChemicalAmount(100.00).withMinimumFireProximity(10L)
        .withVicinity(50L).withSquirtPressure(5).withInitialChemicalAmount(10.0)
        .withFillingUpPressure(3).withInitialStatus(AgentStatus.SEARCHING)
        .withActive(false);
    
    try {
    	entityManager.persist(agentEntity);
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
    
    String strQuery = "From AgentEntity";
  	Query query = entityManager.createQuery(strQuery);
    List<AgentEntity> agentEntityList = query.getResultList();
    
    assertTrue(!agentEntityList.isEmpty());
    
    strQuery = "select a From AgentEntity a where a.name like 'Unit Test Agent Entity'";
    query = entityManager.createQuery(strQuery);
    AgentEntity savedAgentEntity 
          = (AgentEntity) query.getSingleResult();
    entityManager.remove(savedAgentEntity);
    
    hibernateUtil.commit();
  }
  
}
