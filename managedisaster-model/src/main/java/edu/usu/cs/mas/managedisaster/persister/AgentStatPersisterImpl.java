package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentStatEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class AgentStatPersisterImpl implements AgentStatPersister{

  private static final Logger LOGGER = Logger.getLogger(AgentStatPersisterImpl.class);
  private static final String DELETE_ALL = "delete from AgentStatEntity";

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public AgentStatPersisterImpl() { }

  public AgentStatPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public List<AgentStatEntity> getAllAgentStats() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select as from AgentStatEntity as";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<AgentStatEntity> agentStatEntityList = query.getResultList();
    return agentStatEntityList;
  }
  
  @Override
  public List<AgentStatEntity> getAgentStat(AgentEntity agent) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select as from AgentStatEntity as where as.agent.id = "+agent.getId();
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<AgentStatEntity> agentStatEntityList  = query.getResultList();
    return agentStatEntityList;
  }

  @Override
  public void save(AgentStatEntity agentStat) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(agentStat);
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    Query query = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = query.executeUpdate();
    LOGGER.info("The number of deleted agent stat records: "+deletedEntities);
  }
}
