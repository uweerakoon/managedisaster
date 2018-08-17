package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionStatEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class CoalitionStatPersisterImpl implements CoalitionStatPersister{

	private static final Logger LOGGER = Logger.getLogger(CoalitionStatPersisterImpl.class);
	private static final String DELETE_ALL = "delete from CoalitionStatEntity";
	
	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  public CoalitionStatPersisterImpl() { }
  
  public CoalitionStatPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<CoalitionStatEntity> getAllCoalitionStats() {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select cs from CoalitionStatEntity cs";
  	Query query = entityManager.createQuery(strQuery);
    List<CoalitionStatEntity> coalitionStatEntityList = query.getResultList();
    return coalitionStatEntityList;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<CoalitionStatEntity> getCoalitionStat(CoalitionEntity coalition) {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select cs from CoalitionStatEntity cs where cs.coalition.id = "+coalition.getId();
  	Query query = entityManager.createQuery(strQuery);
    List<CoalitionStatEntity> coalitionStatEntityList 
    					= query.getResultList();
    return coalitionStatEntityList;
  }
  
  public void save(CoalitionStatEntity coalitionStat) {
  	entityManager = hibernateUtil.getEntityManager();
  	entityManager.persist(coalitionStat);
  }
  
  @Override
  public void cleanup() {
  	entityManager = hibernateUtil.getEntityManager();
    Query qryDeleteAll = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = qryDeleteAll.executeUpdate();
    LOGGER.info("The number of deleted Coalition stat records: "+deletedEntities);
  }
}
