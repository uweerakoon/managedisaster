package edu.usu.cs.mas.managedisaster.persister;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadIntersectionEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class RoadIntersectionPersisterImpl implements RoadIntersectionPersister {
	private static final Logger LOGGER = Logger.getLogger(RoadIntersectionPersisterImpl.class);
	private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  private List<RoadIntersectionEntity> roadIntersectionEntities;
  private Long nextId;
  
  public RoadIntersectionPersisterImpl() { }
  
  public RoadIntersectionPersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<RoadIntersectionEntity> getAllRoadIntersections() {
  	entityManager = hibernateUtil.getEntityManager();
    if(roadIntersectionEntities == null) {
    	String strQuery = "select ri from RoadIntersectionEntity ri";
    	Query query = entityManager.createQuery(strQuery);
      roadIntersectionEntities = query.getResultList();
    }
    return roadIntersectionEntities;
  }
  
  @Override
  public RoadIntersectionEntity getRoadIntersection(RoadEntity road){
    if(roadIntersectionEntities == null) {
      roadIntersectionEntities = getAllRoadIntersections();
    }
    for(RoadIntersectionEntity tempRoadIntersection : roadIntersectionEntities){
      if(tempRoadIntersection.getRoad().getId().equals(road.getId())){
        return tempRoadIntersection;
      }
    }
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select ri from RoadIntersectionEntity ri where ri.road.id = "+road.getId();
  	Query query = entityManager.createQuery(strQuery);
    RoadIntersectionEntity roadIntersectionEntity = (RoadIntersectionEntity) query.getSingleResult();
    return roadIntersectionEntity;
  }
  
  @Override
  public Long nextId(){
    if(nextId == null){
    	entityManager = hibernateUtil.getEntityManager();
    	String strQuery = "select max(ri.id) from RoadIntersectionEntity ri";
    	Query query = entityManager.createQuery(strQuery);
      nextId = (Long) query.getSingleResult();
    }
    return ++nextId;
  }
  
  @Override
  public void addAllToCache(List<RoadIntersectionEntity> roadIntersections){
    roadIntersectionEntities.addAll(roadIntersections);
  }
  
  @Override
  public RoadEntity getRoad(IntersectionEntity node1, IntersectionEntity node2){
    for(RoadIntersectionEntity roadIntersection : roadIntersectionEntities) {
      if( (roadIntersection.getSourceIntersection().getId() == node1.getId() 
            && roadIntersection.getDestinationIntersection().getId() == node2.getId())
         || (roadIntersection.getSourceIntersection().getId() == node2.getId() 
            && roadIntersection.getDestinationIntersection().getId() == node1.getId())) {
        return roadIntersection.getRoad();
      }
    }
    return null;
  }
  
  @Override
  public RoadIntersectionEntity deleteFromCache(Long id) {
  	RoadIntersectionEntity deletedEntity = null;
  	for(Iterator<RoadIntersectionEntity> roadInteIter = roadIntersectionEntities.listIterator();
  			roadInteIter.hasNext();) {
  		RoadIntersectionEntity tempEntity = roadInteIter.next();
  		if(id.equals(tempEntity.getId())) {
  			deletedEntity = tempEntity;
  			roadInteIter.remove();
  			break;
  		}
  	}
  	LOGGER.info("Deleting Road Intersection: "+deletedEntity);
  	return deletedEntity;
  }
  
  @Override
	public RoadIntersectionEntity delete(Long id) {
  	entityManager = hibernateUtil.getEntityManager();
  	RoadIntersectionEntity deletedEntity = null;
  	deletedEntity = deleteFromCache(id);
  	RoadIntersectionEntity dbEntity = entityManager.find(RoadIntersectionEntity.class, id);
  	if(dbEntity != null) {
  		deletedEntity = dbEntity;
  		entityManager.remove(dbEntity);
  	}
  	LOGGER.info("Deleting Road Intersection: "+deletedEntity);
  	return deletedEntity;
  }
}
