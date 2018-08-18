package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
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
  public List<ForestEntity> getAllBuildings() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select b from BuildingEntity b";
    Query query = entityManager.createQuery(strQuery);
    List<ForestEntity> buildings = query.getResultList();
    return buildings;
  }

  @Override
  public ForestEntity getBuilding(String name) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select b from BuildingEntity b where lower(b.name) like '%"+name.toLowerCase()+"%'";
    Query query = entityManager.createQuery(strQuery);
    ForestEntity building = null;
    try {
      building = (ForestEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("Problem loading single house with name: "+name, e);
    }
    return building;
  }

  @Override
  public ForestEntity getBuilding(Long id) {
    entityManager = hibernateUtil.getEntityManager();
    ForestEntity building = entityManager.find(ForestEntity.class, id);
    return building;
  }

  @Override
  public void save(ForestEntity building) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(building);
    hibernateUtil.commit();
  }

}
