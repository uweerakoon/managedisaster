package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.entity.RoadIntersectionEntity;

public interface RoadIntersectionPersister {

  public List<RoadIntersectionEntity> getAllRoadIntersections();

  public Long nextId();
  
  public RoadIntersectionEntity getRoadIntersection(RoadEntity road);
  
  public void addAllToCache(List<RoadIntersectionEntity> roadIntersections);
  
  public RoadEntity getRoad(IntersectionEntity node1, IntersectionEntity node2);
  
  public RoadIntersectionEntity deleteFromCache(Long id);
  
  public RoadIntersectionEntity delete(Long id);
}
