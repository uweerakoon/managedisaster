package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class BuildingPersisterImpl implements BuildingPersister{
  
  private static final Logger LOGGER = Logger.getLogger(BuildingPersisterImpl.class);

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public BuildingPersisterImpl() { }

  public BuildingPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BuildingEntity> getAllBuildings() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select b from BuildingEntity b";
    Query query = entityManager.createQuery(strQuery);
    List<BuildingEntity> buildings = query.getResultList();
    return buildings;
  }

  @Override
  public BuildingEntity getBuilding(String name) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select b from BuildingEntity b where lower(b.name) like '%"+name.toLowerCase()+"%'";
    Query query = entityManager.createQuery(strQuery);
    BuildingEntity building = null;
    try {
      building = (BuildingEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("Problem loading single house with name: "+name, e);
    }
    return building;
  }

  @Override
  public BuildingEntity getBuilding(Long id) {
    entityManager = hibernateUtil.getEntityManager();
    BuildingEntity building = entityManager.find(BuildingEntity.class, id);
    return building;
  }

  @Override
  public void save(BuildingEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(building);
    hibernateUtil.commit();
  }

}
