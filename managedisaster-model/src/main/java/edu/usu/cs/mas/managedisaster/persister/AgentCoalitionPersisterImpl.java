package edu.usu.cs.mas.managedisaster.persister;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.AgentCoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class AgentCoalitionPersisterImpl implements AgentCoalitionPersister{

  private static final Logger LOGGER = Logger.getLogger(AgentCoalitionPersisterImpl.class);
  private static final String DELETE_ALL = "delete from AgentCoalitionEntity";
  private static final String DELETE_BY_AGENT = "delete from AgentCoalitionEntity where agent.id = ";
  
  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public AgentCoalitionPersisterImpl() { }

  public AgentCoalitionPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @Override
  public void delete(AgentEntity agent) {
    entityManager = hibernateUtil.getEntityManager();
    long agentId = agent.getId();
    Query query = entityManager.createQuery(DELETE_BY_AGENT + agentId);
    int deletedEntities = query.executeUpdate();
    LOGGER.info("The number of deleted agent-coalition records for agent: "+agentId+ " is "+deletedEntities);
    hibernateUtil.commit();
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    Query query = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = query.executeUpdate();
    hibernateUtil.commit();
    LOGGER.info("The number of deleted agent-coalition records: "+deletedEntities);
  }

  @Override
  public void save(AgentCoalitionEntity agentCoalition) {
    entityManager = hibernateUtil.getEntityManager();
    agentCoalition.setTime(new Date());
    entityManager.persist(agentCoalition);
    hibernateUtil.commit();
  }
}
