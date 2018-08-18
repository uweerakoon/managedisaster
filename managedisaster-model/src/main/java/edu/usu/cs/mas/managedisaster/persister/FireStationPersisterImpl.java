package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class FireStationPersisterImpl implements FireStationPersister {

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public FireStationPersisterImpl() { }

  public FireStationPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public FireStationEntity getFireStation(Long id) {
    FireStationEntity fireStationEntity = null;
    entityManager = hibernateUtil.getEntityManager();
    fireStationEntity = entityManager.find(FireStationEntity.class, id);
    return fireStationEntity;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<FireStationEntity> getAllActiveFireStations(){
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select fs from FireStationEntity fs where fs.active = true";
    Query query = entityManager.createQuery(strQuery);
    List<FireStationEntity>	fireStations = query.getResultList();
    return fireStations;
  }

  @Override
  public FireStationEntity getClosestFireStation(double x, double y, Long vicinity) {
    List<FireStationEntity> fireStations = getAllActiveFireStations();
    double minDistance = Double.POSITIVE_INFINITY;
    FireStationEntity closeStation = null;
    for(FireStationEntity entity : fireStations) {
      double distance = Math.sqrt(((entity.getStationX() - x) * (entity.getStationX() - x))
        + ((entity.getStationY() - y) * (entity.getStationY() - y)));
      if(distance > vicinity) {
        continue;
      }
      if(distance < minDistance) {
        minDistance = distance;
        closeStation = entity;
      }
    }
    return closeStation;
  }

  @Override
  public FireStationEntity save(FireStationEntity fireStation) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(fireStation);
    return fireStation;
  }

  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    List<FireStationEntity> fireStations = getAllActiveFireStations();
    for(FireStationEntity fireStation : fireStations) {
      fireStation.setChemicalAmount(fireStation.getInitialChemicalAmount());
      fireStation.setOutOfService(false);
      entityManager.persist(fireStation);
    }
    hibernateUtil.commit();
  }
}
