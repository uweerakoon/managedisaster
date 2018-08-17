package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.common.CoalitionBuildingStatus;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
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
  public List<CoalitionBuildingEntity> getFeasibleCoalBuilds() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
                    + "where cb.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionBuildingEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<CoalitionBuildingEntity> getUnallocatedCoalBuildByCoalIds(List<Long> coalitionIds) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.coalition.id in (:coalitionIds)"
      + " and (cb.status is null or (cb.status != 'CANCEL' and cb.status != 'ALLOCATED'))";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("coalitionIds", coalitionIds);
    @SuppressWarnings("unchecked")
    List<CoalitionBuildingEntity> coalitionBuildingEntityList = query.getResultList();
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
  public List<CoalitionBuildingEntity> getUnallocatedCoalBuildByBuildIds(List<Long> buildIds) {
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
    List<CoalitionBuildingEntity> coalitionBuildingEntityList = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<CoalitionBuildingEntity> getCoalitionBuildings(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.coalition.id = "+coalition.getId()
      +" and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionBuildingEntity> coalitionBuildingEntityList = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public List<BuildingEntity> getBuildings(CoalitionEntity coalition) {
    List<CoalitionBuildingEntity> coalitionBuildingEntityList = getCoalitionBuildings(coalition);
    List<BuildingEntity> buildings = coalitionBuildingEntityList.stream()
                                      .map(cs -> cs.getBuilding())
                                      .collect(Collectors.toList());
    return buildings;
  }
  
  @Override
  public List<CoalitionBuildingEntity> getCoalitionBuildings(BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      +" and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionBuildingEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public CoalitionBuildingEntity getAllocatedCoalBuild(BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.building.id = "+building.getId()
        +" and cb.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionBuildingEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionBuildingEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      LOGGER.info("No allocated coalition-build pair found for build: "+building.getId());
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionBuildingEntity getExecutingCoalBuild(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.coalition.id = "+coalition.getId()
        +" and cb.status = 'EXECUTING'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionBuildingEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionBuildingEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      return null;
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionBuildingEntity getAllocatedCoalBuild(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.coalition.id = "+coalition.getId()
        +" and cb.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionBuildingEntity coalitionBuildingEntity  = null;
    try {
      coalitionBuildingEntity = (CoalitionBuildingEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      throw nre;
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public List<CoalitionEntity> getCoalitions(BuildingEntity building) {
    List<CoalitionBuildingEntity> coalitionBuildingEntityList = getCoalitionBuildings(building);
    List<CoalitionEntity> coalitions = coalitionBuildingEntityList.stream()
                                        .map(cs -> cs.getCoalition())
                                        .collect(Collectors.toList());
    return coalitions;
  }
  
  @Override
  public List<CoalitionBuildingEntity> findBestUtilityCoalitionBuildings(BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb where cb.utility = "
                    + "(select max(utility) from CoalitionBuildingEntity "
                    + "where building.id = "+building.getId()+""
                    + " and coalition.allocatedBuilding is null"
                    + " and (status is null or status != 'CANCEL'))";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionBuildingEntity> coalitionBuildingEntityList  = query.getResultList();
    return coalitionBuildingEntityList;
  }
  
  @Override
  public CoalitionBuildingEntity getCoalitionBuilding(CoalitionEntity coalition, BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      + " and cb.coalition.id = "+coalition.getId()
      + " and (cb.status is null or cb.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    CoalitionBuildingEntity coalitionBuildingEntity = (CoalitionBuildingEntity) query.getSingleResult();
    return coalitionBuildingEntity;
  }
  
  @Override
  public CoalitionBuildingEntity getFeasibleCoalBuilds(CoalitionEntity coalition, BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cb from CoalitionBuildingEntity cb "
      + "where cb.building.id = "+building.getId()
      + " and cb.coalition.id = "+coalition.getId()
      + " and cb.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionBuildingEntity coalitionBuildingEntity = null;
    try {
      coalitionBuildingEntity = (CoalitionBuildingEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("cannot find feasible coal-build pairs for coal Id: "+coalition.getId()+" build id: "+building.getId(), e);
    }
    return coalitionBuildingEntity;
  }
  
  @Override
  public List<BuildingEntity> getOneCoalitionBuildings() {
    String strQuery = "select cb.building " + 
        "from CoalitionBuildingEntity cb " + 
        "group by cb.building, cb.status " + 
        "having count(cb.building) = 1 and cb.status = '"+CoalitionBuildingStatus.UTILIZED+"'"; 
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<BuildingEntity> buildings = query.getResultList();
    return buildings;
  }
  
  
  @Override
  public void save(CoalitionBuildingEntity coalitionBuilding) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(coalitionBuilding);
    hibernateUtil.commit();
  }
  
  @Override
  public void cancel(CoalitionBuildingEntity coalitionBuilding) {
    coalitionBuilding.setStatus(CoalitionBuildingStatus.CANCEL);
    save(coalitionBuilding);
  }
  
  @Override
  public void delete(CoalitionBuildingEntity coalitionBuilding) {
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
