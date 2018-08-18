package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;

public interface ForestPersister {
  /**
   * Fetch all the buildings
   * @return list of all buildings
   */
  public List<ForestEntity> getAllForests();
  
  public ForestEntity getForest(String name);
  
  public ForestEntity getForest(Long id);
  
  public void save(ForestEntity forest);
}
