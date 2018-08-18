package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningForestStatEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class BurningForestStatPersisterImpl implements BurningForestStatPersister{

	private static final Logger LOGGER = Logger.getLogger(BurningForestStatPersisterImpl.class);
	private static final String DELETE_ALL = "delete from BurningForestStatEntity";
	
	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  public BurningForestStatPersisterImpl() { }
  
  public BurningForestStatPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<BurningForestStatEntity> getAllBurningForestStats() {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select fs from BurningForestStatEntity fs";
  	Query query = entityManager.createQuery(strQuery);
    List<BurningForestStatEntity> burningForestStatEntityList 
    																	= query.getResultList();
    
    return burningForestStatEntityList;
    
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<BurningForestStatEntity> getBurningForestStat(ForestEntity forest) {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select bs from BurningForestStatEntity fs where fs.forest.id = "+forest.getId();
  	Query query = entityManager.createQuery(strQuery);
    List<BurningForestStatEntity> burningForestStatEntityList 
    					= query.getResultList();
    return burningForestStatEntityList;
  }
  
  @Override
  public void save(BurningForestStatEntity burningForestStat) {
  	entityManager = hibernateUtil.getEntityManager();
  	entityManager.persist(burningForestStat);
  }
  
  @Override
  public void cleanup() {
  	entityManager = hibernateUtil.getEntityManager();
    Query qryDeleteAll = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = qryDeleteAll.executeUpdate();
    LOGGER.info("The number of deleted burning forest stat records: "+deletedEntities);
  }
}
