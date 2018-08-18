package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningForestStatEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class BurningBuildingStatPersisterImpl implements BurningBuildingStatPersister{

	private static final Logger LOGGER = Logger.getLogger(BurningBuildingStatPersisterImpl.class);
	private static final String DELETE_ALL = "delete from BurningBuildingStatEntity";
	
	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  public BurningBuildingStatPersisterImpl() { }
  
  public BurningBuildingStatPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<BurningForestStatEntity> getAllBurningBuildingStats() {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select bs from BurningBuildingStatEntity bs";
  	Query query = entityManager.createQuery(strQuery);
    List<BurningForestStatEntity> burningBuildingStatEntityList 
    																	= query.getResultList();
    
    return burningBuildingStatEntityList;
    
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<BurningForestStatEntity> getBurningBuildingStat(ForestEntity building) {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select bs from BurningBuildingStatEntity bs where bs.building.id = "+building.getId();
  	Query query = entityManager.createQuery(strQuery);
    List<BurningForestStatEntity> burningBuildingStatEntityList 
    					= query.getResultList();
    return burningBuildingStatEntityList;
  }
  
  @Override
  public void save(BurningForestStatEntity burningBuildingStat) {
  	entityManager = hibernateUtil.getEntityManager();
  	entityManager.persist(burningBuildingStat);
  }
  
  @Override
  public void cleanup() {
  	entityManager = hibernateUtil.getEntityManager();
    Query qryDeleteAll = entityManager.createQuery(DELETE_ALL);
    int deletedEntities = qryDeleteAll.executeUpdate();
    LOGGER.info("The number of deleted burning building stat records: "+deletedEntities);
  }
}
