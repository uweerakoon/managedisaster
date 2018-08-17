package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;

public interface RoadPersister {
  /**
   * Return all the roads from the persister
   * @return list of all roads
   */
  public List<RoadEntity> getAllRoads();
  /**
   * get the road by the id
   * @param id
   * @return
   */
  public RoadEntity getRoad(Long id);
  
  public RoadEntity getRoad(MutableInt2D coordinate);
  
  public Long nextId();
  
  public void addAllToCache(List<RoadEntity> roads);
  
  public RoadEntity deleteFromCache(Long roadId);
  
  public RoadEntity delete(Long roadId);
}
