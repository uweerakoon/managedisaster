package edu.usu.cs.mas.managedisaster.persister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import sim.util.MutableInt2D;

public class RoadPersisterImpl implements RoadPersister{
	private static final Logger LOGGER = Logger.getLogger(RoadPersisterImpl.class);
	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  private Map<Long, RoadEntity> roadEntities;
  private Long nextId;
  
  public RoadPersisterImpl() { }
  
  public RoadPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @Override
  public List<RoadEntity> getAllRoads(){
    
    if(roadEntities == null) {
      initializeRoads(); 
    }
    
    return new ArrayList<RoadEntity>(roadEntities.values());
  }
  
  @SuppressWarnings("unchecked")
  private void initializeRoads(){
  	entityManager = hibernateUtil.getEntityManager();
    roadEntities = new HashMap<Long, RoadEntity>();
    String strQuery = "select r from RoadEntity r";
  	Query query = entityManager.createQuery(strQuery);
    List<RoadEntity> roads = query.getResultList();
    for(RoadEntity road : roads){
      roadEntities.put(road.getId(), road);
    }
  }
  
  @Override
  public RoadEntity getRoad(Long id){
    
    if(roadEntities == null) {
      initializeRoads();
    }
    
    if(roadEntities.containsKey(id)){
      return roadEntities.get(id);
    }
    
    entityManager = hibernateUtil.getEntityManager();
    RoadEntity roadEntity = entityManager.find(RoadEntity.class, id);
    
    return roadEntity;
  }
  
  /**
   * Use road canvas to get the id and then use the id to retrieve the road entity
   */
  @Deprecated
  @Override
  public RoadEntity getRoad(MutableInt2D coordinate){
    if(roadEntities == null) {
      initializeRoads();
    }
    
    for(RoadEntity tempRoad : roadEntities.values()){
      if(tempRoad.contains(coordinate)){
        return tempRoad;
      }
    }
    
    return null;
  }
  
  @Override
  public Long nextId(){
    if(nextId == null){
    	entityManager = hibernateUtil.getEntityManager();
    	String strQuery = "select max(r.id) from RoadEntity r";
    	Query query = entityManager.createQuery(strQuery);
      nextId = (Long) query.getSingleResult();
    }
    return ++nextId;
  }
  
  @Override
  public void addAllToCache(List<RoadEntity> roads){
    for(RoadEntity road : roads){
      roadEntities.put(road.getId(), road);
    }
  }
  
  @Override
  public RoadEntity deleteFromCache(Long roadId) {
  	RoadEntity roadEntity = null;
  	if(roadEntities.containsKey(roadId)) {
  		roadEntity = roadEntities.remove(roadId);
  	}
  	return roadEntity;
  }
  
  @Override
  public RoadEntity delete(Long roadId) {
  	entityManager = hibernateUtil.getEntityManager();
  	RoadEntity roadEntity = deleteFromCache(roadId);
  	RoadEntity dbRoad = entityManager.find(RoadEntity.class, roadId);
  	if(dbRoad != null) {
  		roadEntity = dbRoad;
  		entityManager.remove(dbRoad);
  	}
  	LOGGER.info("Deleteing Road Entity: "+roadEntity);
  	return roadEntity;
  }
  
}
