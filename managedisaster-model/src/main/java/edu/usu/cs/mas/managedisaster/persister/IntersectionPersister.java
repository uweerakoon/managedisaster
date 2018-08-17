package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import sim.util.Bag;
import sim.util.MutableDouble2D;
import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;

public interface IntersectionPersister {
  
  public List<IntersectionEntity> getAllIntersection();
  
  public IntersectionEntity getIntersection(Long id);
  
  public Long nextId();
  
  public void addToCache(IntersectionEntity intersection);

  public IntersectionEntity getCloseEntity(MutableDouble2D currentPlace, Bag ids, double distance);
  
  public IntersectionEntity delete(Long id);
  
  public IntersectionEntity deleteFromCache(Long id);
}
