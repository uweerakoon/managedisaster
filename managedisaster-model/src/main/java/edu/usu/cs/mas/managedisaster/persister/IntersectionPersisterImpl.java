package edu.usu.cs.mas.managedisaster.persister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.MutableDouble2D;

public class IntersectionPersisterImpl implements IntersectionPersister {
  private static final Logger LOGGER = Logger.getLogger(IntersectionPersisterImpl.class);
  private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  private Map<Long, IntersectionEntity> intersectionEntities;
  private Long nextId;
  
  public IntersectionPersisterImpl() { }
  
  public IntersectionPersisterImpl(HibernateUtil hibernateUtil) {
    this.hibernateUtil = hibernateUtil;
  }
  
  @Override
  public List<IntersectionEntity> getAllIntersection(){
    if(intersectionEntities == null) {
      initializeIntersections();
    }
    return new ArrayList<IntersectionEntity>(intersectionEntities.values());
  }
  
  @SuppressWarnings("unchecked")
  private void initializeIntersections(){
  	entityManager = hibernateUtil.getEntityManager();
    intersectionEntities = new HashMap<Long, IntersectionEntity>();
    String strQuery = "select i from IntersectionEntity i";
  	Query query = entityManager.createQuery(strQuery);
    List<IntersectionEntity> intersections = query.getResultList();
    for(IntersectionEntity intersection : intersections){
      intersectionEntities.put(intersection.getId(), intersection);
    }
  }
  
  /**
   * Need to have a cache because some of the intersection are temporarily 
   * inserted to the system.
   */
  @Override
  public IntersectionEntity getIntersection(Long id){
    
    if(intersectionEntities == null) {
      initializeIntersections();
    }
    
    if(intersectionEntities.containsKey(id)){
        return intersectionEntities.get(id);
    }
    
    entityManager = hibernateUtil.getEntityManager();
    IntersectionEntity intersection = entityManager.find(IntersectionEntity.class, id);
    
    return intersection;
  }
  
  @Override
  public Long nextId(){
    if(nextId == null){
    	entityManager = hibernateUtil.getEntityManager();
    	String strQuery = "select max(i.id) from IntersectionEntity i";
    	Query query = entityManager.createQuery(strQuery);
      nextId = (Long) query.getSingleResult();
    }
    return ++nextId;
  }
  
  @Override
  public void addToCache(IntersectionEntity intersection){
  	if(intersectionEntities ==  null){
      initializeIntersections();
    }
    intersectionEntities.put(intersection.getId(), intersection);
  }
  
  @Override
  public IntersectionEntity getCloseEntity(MutableDouble2D currentPlace, Bag ids, double distance){
    IntersectionEntity closeIntersection = null;
    
    if(intersectionEntities ==  null){
      initializeIntersections();
    }
    
    for(int i=0; i < ids.numObjs; i++){
      Long id = (Long) ids.objs[i];
      IntersectionEntity intersection = intersectionEntities.get(id);
      double tempDistance = currentPlace.distance(new Double2D(intersection.getX(),intersection.getY()));
    
      if(distance == -1 || distance > tempDistance){
        distance = tempDistance;
        closeIntersection = intersection;
      }
      
    }
    
    return closeIntersection;
  }
  
  @Override
  public IntersectionEntity delete(Long id) {
  	entityManager = hibernateUtil.getEntityManager();
  	IntersectionEntity deletedEntity = deleteFromCache(id);
  	IntersectionEntity dbEntity = (IntersectionEntity) entityManager.find(IntersectionEntity.class, id);
  	if(dbEntity != null) {
  		deletedEntity = dbEntity;
  		entityManager.remove(dbEntity);
  	}
  	LOGGER.info("Deleting Intersection: "+deletedEntity);
  	return deletedEntity;
  }

  @Override
  public IntersectionEntity deleteFromCache(Long id) {
  	IntersectionEntity deletedEntity = null;
  	if(intersectionEntities.containsKey(id)) {
  		deletedEntity = intersectionEntities.get(id);
  		intersectionEntities.remove(id);
  	}
  	LOGGER.info("Deleting Intersection: "+deletedEntity);
  	return deletedEntity;
  }
}
