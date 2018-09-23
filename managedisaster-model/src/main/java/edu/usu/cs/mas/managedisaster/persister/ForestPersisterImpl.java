package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class ForestPersisterImpl implements ForestPersister{
  
  private static final Logger LOGGER = Logger.getLogger(ForestPersisterImpl.class);

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  public ForestPersisterImpl() { }

  public ForestPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ForestEntity> getAllForests() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select f from ForestEntity f";
    Query query = entityManager.createQuery(strQuery);
    List<ForestEntity> forests = query.getResultList();
    return forests;
  }
  
  @Override
  public ForestEntity getForest(int x, int y) {
    List<ForestEntity> forests = getAllForests();
    for(ForestEntity forest : forests) {
      if(forest.contains(x, y)) {
        return forest;
      }
    }
    return null;
  }

  @Override
  public ForestEntity getForest(String name) {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select f from ForestEntity f where lower(f.name) like '%"+name.toLowerCase()+"%'";
    Query query = entityManager.createQuery(strQuery);
    ForestEntity forest = null;
    try {
      forest = (ForestEntity) query.getSingleResult();
    }
    catch(Exception e) {
      LOGGER.error("Problem loading single house with name: "+name, e);
    }
    return forest;
  }

  @Override
  public ForestEntity getForest(Long id) {
    entityManager = hibernateUtil.getEntityManager();
    ForestEntity forest = entityManager.find(ForestEntity.class, id);
    return forest;
  }

  @Override
  public void save(ForestEntity forest) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(forest);
    hibernateUtil.commit();
  }

}
