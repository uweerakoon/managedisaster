package edu.usu.cs.mas.managedisaster.temporary;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
//TODO - Need to delete this class
public class TestHibernateUtil {
  
  static Logger logger = Logger.getLogger(TestHibernateUtil.class);

  public static void main(String[] args) {
    /*Session session = HibernateUtil.getSessionFactory().openSession();
    session.beginTransaction();
    
//    AgentEntity ae = new AgentEntity();
//    session.save(ae);
    
    AgentEntity agentEntity = new AgentEntity().withX(20.5).withY(5.7)
        .withName("Test Agent Entity")
        .withStatus(AgentStatus.SEARCHING).withRole(AgentRole.INDEPENDENT)
        .withColor(-16711936).withSpeed(34.6);
    session.save(agentEntity);
    
    session.getTransaction().commit();
    
    Query query = session.createQuery("From AgentEntity");
    
    List<AgentEntity> agentEntityList = query.list();
    
    logger.info("udara size of agent entity list: "+agentEntityList.size());
    
    HibernateUtil.shutdown();*/
  }
}
