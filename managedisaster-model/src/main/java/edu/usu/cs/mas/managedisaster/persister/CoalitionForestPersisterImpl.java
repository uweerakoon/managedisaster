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

public class CoalitionForestPersisterImpl implements CoalitionForestPersister  {

  private static final Logger LOGGER = Logger.getLogger(CoalitionForestPersisterImpl.class);
  
  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;
  
  public CoalitionForestPersisterImpl() { }
  
  public CoalitionForestPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }
  
  @Override
  public List<CoalitionForestEntity> getFeasibleCoalForests() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
                    + "where cf.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList  = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public List<CoalitionForestEntity> getUnallocatedCoalForestByCoalIds(List<Long> coalitionIds) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.coalition.id in (:coalitionIds)"
      + " and (cf.status is null or (cf.status != 'CANCEL' and cf.status != 'ALLOCATED'))";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("coalitionIds", coalitionIds);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public List<Integer> getCancelledCoalitionIds() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select distinct(cf.coalition_id) " + 
        "from managedisaster.coalition_forest cf " + 
        "where cf.coalition_id not in " + 
        "(select distinct(coalition_id) " + 
        "from managedisaster.coalition_forest " + 
        "where status != 'CANCEL')"; 
      Query query = entityManager.createNativeQuery(strQuery);
      @SuppressWarnings("unchecked")
      List<Integer> cancelCoalitionIds = query.getResultList();
      return cancelCoalitionIds;
  }
  
  @Override
  public List<CoalitionForestEntity> getUnallocatedCoalForestByForestIds(List<Long> forestIds) {
    if(forestIds.isEmpty()) {
      LOGGER.error("Empty forest id is not expected");
      throw new ManageDisasterServiceException("Empty forest id is not expected");
    }
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.forest.id in (:forestIds)"
      + " and (cf.status is null or (cf.status != 'CANCEL' and cf.status != 'ALLOCATED'))";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("forestIds", forestIds);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public List<CoalitionForestEntity> getCoalitionForests(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.coalition.id = "+coalition.getId()
      +" and (cf.status is null or cf.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public List<ForestEntity> getForests(CoalitionEntity coalition) {
    List<CoalitionForestEntity> coalitionForestEntityList = getCoalitionForests(coalition);
    List<ForestEntity> forests = coalitionForestEntityList.stream()
                                      .map(cs -> cs.getForest())
                                      .collect(Collectors.toList());
    return forests;
  }
  
  @Override
  public List<CoalitionForestEntity> getCoalitionForests(ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.forest.id = "+forest.getId()
      +" and (cf.status is null or cf.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList  = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public CoalitionForestEntity getAllocatedCoalForest(ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
        + "where cf.forest.id = "+forest.getId()
        +" and cf.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionForestEntity  = null;
    try {
      coalitionForestEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      LOGGER.info("No allocated coalition-forest pair found for forest: "+forest.getId());
    }
    return coalitionForestEntity;
  }
  
  @Override
  public CoalitionForestEntity getExecutingCoalForest(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
        + "where cf.coalition.id = "+coalition.getId()
        +" and cf.status = 'EXECUTING'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionForestEntity  = null;
    try {
      coalitionForestEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      return null;
    }
    return coalitionForestEntity;
  }
  
  @Override
  public CoalitionForestEntity getAllocatedCoalForest(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
        + "where cf.coalition.id = "+coalition.getId()
        +" and cf.status = 'ALLOCATED'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionForestEntity  = null;
    try {
      coalitionForestEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(NoResultException nre) {
      throw nre;
    }
    return coalitionForestEntity;
  }
  
  @Override
  public List<CoalitionEntity> getCoalitions(ForestEntity forest) {
    List<CoalitionForestEntity> coalitionForestEntityList = getCoalitionForests(forest);
    List<CoalitionEntity> coalitions = coalitionForestEntityList.stream()
                                        .map(cs -> cs.getCoalition())
                                        .collect(Collectors.toList());
    return coalitions;
  }
  
  @Override
  public List<CoalitionForestEntity> findBestUtilityCoalitionForests(ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf where cf.utility = "
                    + "(select max(utility) from CoalitionForestEntity "
                    + "where forest.id = "+forest.getId()+""
                    + " and coalition.allocatedForest is null"
                    + " and (status is null or status != 'CANCEL'))";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionForestEntity> coalitionForestEntityList  = query.getResultList();
    return coalitionForestEntityList;
  }
  
  @Override
  public CoalitionForestEntity getCoalitionForest(CoalitionEntity coalition, ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.forest.id = "+forest.getId()
      + " and cf.coalition.id = "+coalition.getId()
      + " and (cf.status is null or cf.status != 'CANCEL')";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionForestEntity = (CoalitionForestEntity) query.getSingleResult();
    return coalitionForestEntity;
  }
  
  @Override
  public CoalitionForestEntity getFeasibleCoalForests(CoalitionEntity coalition, ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select cf from CoalitionForestEntity cf "
      + "where cf.forest.id = "+forest.getId()
      + " and cf.coalition.id = "+coalition.getId()
      + " and cf.status = 'FEASIBLE'";
    Query query = entityManager.createQuery(strQuery);
    CoalitionForestEntity coalitionForestEntity = null;
    try {
      coalitionForestEntity = (CoalitionForestEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("cannot find feasible coal-forest pairs for coal Id: "+coalition.getId()+" forest id: "+forest.getId(), e);
    }
    return coalitionForestEntity;
  }
  
  @Override
  public List<ForestEntity> getOneCoalitionForests() {
    String strQuery = "select cf.forest " + 
        "from CoalitionForestEntity cf " + 
        "group by cf.forest, cf.status " + 
        "having count(cf.forest) = 1 and cf.status = '"+CoalitionForestStatus.UTILIZED+"'"; 
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<ForestEntity> forests = query.getResultList();
    return forests;
  }
  
  
  @Override
  public void save(CoalitionForestEntity coalitionForest) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(coalitionForest);
    hibernateUtil.commit();
  }
  
  @Override
  public void cancel(CoalitionForestEntity coalitionForest) {
    coalitionForest.setStatus(CoalitionForestStatus.CANCEL);
    save(coalitionForest);
  }
  
  @Override
  public void delete(CoalitionForestEntity coalitionForest) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.remove(coalitionForest);
    hibernateUtil.commit();
  }
  
  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    Query query = entityManager.createQuery("delete from CoalitionForestEntity");
    int deletedEntities = query.executeUpdate();
    LOGGER.info("The number of deleted coalition-forest pair records: "+deletedEntities);
  }
}
