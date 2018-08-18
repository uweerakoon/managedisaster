package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.common.CoalitionForestStatus;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.exception.ManageDisasterServiceException;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class CoalitionBuildingPersisterImpl implements CoalitionBuildingPersister  {

  private static final Logger LOGGER = Logger.getLogger(CoalitionBuildingPersisterImpl.class);
  
  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;
  
  public CoalitionBuildingPersisterImpl() { }
  
  public CoalitionBuildingPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }
  
  @Override
  public List<CoalitionForestEntity> getFeasibleCoalBuilds() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
                    + "where cb.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<CoalitionForestEntity> getUnallocatedCoalBuildByCoalIds(List<Long> coalitionIds) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.coalition.id in (:coalitionIds)"
      + " and (cb.status is null or (cb.status != 'CANCEL' and cb.status != 'ALLOCATED'))";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("coalitionIds", coalitionIds);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<Integer> getCancelledCoalitionIds() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select distinct(cb.coalition_id) " + 
        "from managedisaster.coalition_building cb " + 
        "where cb.coalition_id not in " + 
        "(select distinct(coalition_id) " + 
        "from managedisaster.coalition_building " + 
        "where status != 'CANCEL')"; 
      Query query = entityManager.createNativeQuery(strQuery);
      @SuppressWarnings("unchecked")
      List<Integer> cancelCoalitionIds = query.getResultList();
      return cancelCoalitionIds;
  }
  
  @Override
  public List<CoalitionForestEntity> getUnallocatedCoalBuildByBuildIds(List<Long> buildIds) {
    if(buildIds.isEmpty()) {
      LOGGER.error("Empty builidng id is not expected");
      throw new ManageDisasterServiceException("Empty builidng id is not expected");
    }
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id in (:buildIds)"
      + " and (cb.status is null or (cb.status != 'CANCEL' and cb.status != 'ALLOCATED'))";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("buildIds", buildIds);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<CoalitionForestEntity> getCoalitionBuildings(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.coalition.id = "+coalition.getId()
      +" and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<ForestEntity> getBuildings(CoalitionEntity coalition) {
    List<CoalitionForestEntity> coalitionBuildingEntityList = getCoalitionBuildings(coalition);
    List<ForestEntity> buildings = coalitionBuildingEntityList.stream()
                                      .map(cs -> cs.getForest())
                                      .collect(Collectors.toList());
    return buildings;
  }
  
  @Override
  public List<CoalitionForestEntity> getCoalitionBuildings(ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      +" and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public CoalitionForestEntity getAllocatedCoalBuild(ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.building.id = "+building.getId()
        +" and cb.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      LOGGER.info("No allocated coalition-build pair found for build: "+building.getId());
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionForestEntity getExecutingCoalBuild(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.coalition.id = "+coalition.getId()
        +" and cb.status = 'EXECUTING'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      return null;
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionForestEntity getAllocatedCoalBuild(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.coalition.id = "+coalition.getId()
        +" and cb.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      throw nre;
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public List<CoalitionEntity> getCoalitions(ForestEntity building) {
    List<CoalitionForestEntity> coalitionBuildingEntityList = getCoalitionBuildings(building);
    List<CoalitionEntity> coalitions = coalitionBuildingEntityList.stream()
                                        .map(cs -> cs.getCoalition())
                                        .collect(Collectors.toList());
    return coalitions;
  }
  
  @Override
  public List<CoalitionForestEntity> findBestUtilityCoalitionBuildings(ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb where cb.utility = "
                    + "(select max(utility) from CoalitionBuildingEntity "
                    + "where building.id = "+building.getId()+""
                    + " and coalition.allocatedBuilding is null"
                    + " and (status is null or status != 'CANCEL'))";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public CoalitionForestEntity getCoalitionBuilding(CoalitionEntity coalition, ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      + " and cb.coalition.id = "+coalition.getId()
      + " and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionBuildingEntity = (CoalitionForestEntity) query.getSingleResult();
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionForestEntity getFeasibleCoalBuilds(CoalitionEntity coalition, ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      + " and cb.coalition.id = "+coalition.getId()
      + " and cb.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionBuildingEntity = null;
    try {
      coalitionBuildingEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("cannot find feasible coal-build pairs for coal Id: "+coalition.getId()+" build id: "+building.getId(), e);
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public List<ForestEntity> getOneCoalitionBuildings() {
    String strQuery = "select cb.building " + 
        "from CoalitionBuildingEntity cb " + 
        "group by cb.building, cb.status " + 
        "having count(cb.building) = 1 and cb.status = '"+CoalitionForestStatus.UTILIZED+"'"; 
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<ForestEntity> buildings = query.getResultList();
    return buildings;
  }
  
  
  @Override
  public void save(CoalitionForestEntity coalitionBuilding) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(coalitionBuilding);
    hibernateUtil.commit();
  }
  
  @Override
  public void cancel(CoalitionForestEntity coalitionBuilding) {
    coalitionBuilding.setStatus(CoalitionForestStatus.CANCEL);
    save(coalitionBuilding);
  }
  
  @Override
  public void delete(CoalitionForestEntity coalitionBuilding) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.remove(coalitionBuilding);
    hibernateUtil.commit();
  }
  
  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    Query query = entityManager.createQuery("delete from CoalitionBuildingEntity");
    int deletedEntities = query.executeUpdate();
    LOGGER.info("The number of deleted coalition-building pair records: "+deletedEntities);
  }
}
