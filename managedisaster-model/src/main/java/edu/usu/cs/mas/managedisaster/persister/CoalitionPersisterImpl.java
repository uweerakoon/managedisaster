package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.common.CoalitionStatus;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class CoalitionPersisterImpl implements CoalitionPersister {

  private static final Logger LOGGER = Logger.getLogger(CoalitionPersisterImpl.class);

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public CoalitionPersisterImpl() { }

  public CoalitionPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public CoalitionEntity getCoalition(Long id) {
    entityManager = hibernateUtil.getEntityManager();
    CoalitionEntity coalitionEntity = entityManager.find(CoalitionEntity.class, id);
    return coalitionEntity;
  }

  @Override
  public List<CoalitionEntity> getAllCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public List<CoalitionEntity> getActiveCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c where c.status = '"+CoalitionStatus.EXECUTING+"'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public List<CoalitionEntity> getFormingCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c where c.status = '"+CoalitionStatus.FORMING+"'"
        + " and c.status != 'CANCEL'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public List<CoalitionEntity> getOptimizingCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c where c.status = '"+CoalitionStatus.OPTIMIZING+"'"
        + " and c.status != 'CANCEL'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public List<CoalitionEntity> getFeasibleCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c where c.feasible = true"
        + " and c.status != 'CANCEL'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public List<CoalitionEntity> getFeasibleUnallocatedCoalitions() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select c from CoalitionEntity c where c.feasible = true"
        + " and c.allocatedForest is null and c.status != 'CANCEL'";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }
  
  @Override
  public List<CoalitionEntity> getUnallocatedCoalitions(FireStationEntity fireStation) {
    String strQuery = "select c from CoalitionEntity c where c.fireStation.id = "+fireStation.getId()
        + " and c.allocatedForest is null";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<CoalitionEntity> coalitions = query.getResultList();
    return coalitions;
  }

  @Override
  public void save(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(coalition);
    hibernateUtil.commit();
  }

  @Override
  public void cancel(CoalitionEntity coalition) {
    coalition.setStatus(CoalitionStatus.CANCEL);
    save(coalition);
  }

  @Override
  public void delete(CoalitionEntity coalition) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.remove(coalition);
    hibernateUtil.commit();
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "delete from CoalitionEntity";
    Query query = entityManager.createQuery(strQuery);
    int noCoalitions = query.executeUpdate();
    LOGGER.info("The number of Deleted coalitions: "+noCoalitions);
    hibernateUtil.commit();
  }
}
