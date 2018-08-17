package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class AgentPersisterImpl implements AgentPersister{

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public AgentPersisterImpl() { }

  public AgentPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public List<AgentEntity> getAllActiveAgents(){
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select a from AgentEntity a where a.active = true";
    Query query = entityManager.createQuery(strQuery);

    @SuppressWarnings("unchecked")
    List<AgentEntity> agentEntityList = query.getResultList();

    return agentEntityList;

  }

  @Override
  public AgentEntity getAgent(Long id) {
    AgentEntity agent = null;
    entityManager = hibernateUtil.getEntityManager();
    agent = entityManager.find(AgentEntity.class, id);
    return agent;
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    List<AgentEntity> agentEntityList = getAllActiveAgents();
    for(AgentEntity agent : agentEntityList) {
      agent.setChemicalAmount(agent.getInitialChemicalAmount());
      agent.setCoalition(null);
      entityManager.persist(agent);
    }
    hibernateUtil.commit();
  }

  @Override
  public void save(AgentEntity agent) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(agent);
    hibernateUtil.commit();
  }
}
