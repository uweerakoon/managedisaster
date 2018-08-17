package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentUtilityEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import sim.engine.SimState;

public class AgentUtilityPersisterImpl implements AgentUtilityPersister {
  
  private static final Logger LOGGER = Logger.getLogger(AgentUtilityPersisterImpl.class);
  private static final String DELETE_ALL = "delete from AgentUtilityEntity";

  //Helps to keep the iteration count
  private SimState simState;
  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;
  
  public AgentUtilityPersisterImpl() { }
  
  public AgentUtilityPersisterImpl(HibernateUtil hibernateUtil) { 
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public List<AgentUtilityEntity> getAllAgentUtilities() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select au from AgentUtilityEntity au";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<AgentUtilityEntity> agentUtilityEntities = query.getResultList();
    return agentUtilityEntities;
  }

  @Override
  public List<AgentUtilityEntity> getAgentUtility(AgentEntity agent) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select au from AgentUtilityEntity au where au.agent.id = "+agent.getId();
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<AgentUtilityEntity> agentUtilityEntities  = query.getResultList();
    return agentUtilityEntities;
  }

  @Override
  public void save(AgentUtilityEntity agentUtil) {
    long currentTime = (long)simState.schedule.getTime();
    agentUtil.setTime(currentTime);
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(agentUtil);
    hibernateUtil.commit();
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    Query query = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = query.executeUpdate();
    LOGGER.info("The number of deleted agent utility records: "+deletedEntities);
  }
  
  @Override
  public void setSimState(SimState simState) {
    this.simState = simState;
  }

}
