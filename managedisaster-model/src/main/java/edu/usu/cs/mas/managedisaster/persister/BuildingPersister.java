package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;

public interface BuildingPersister {
  /**
   * Fetch all the buildings
   * @return list of all buildings
   */
  public List<ForestEntity> getAllBuildings();
  
  public ForestEntity getBuilding(String name);
  
  public ForestEntity getBuilding(Long id);
  
  public void save(ForestEntity building);
}
