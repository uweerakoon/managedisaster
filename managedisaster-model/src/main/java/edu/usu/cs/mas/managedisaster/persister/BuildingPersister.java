package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;

public interface BuildingPersister {
  /**
   * Fetch all the buildings
   * @return list of all buildings
   */
  public List<BuildingEntity> getAllBuildings();
  
  public BuildingEntity getBuilding(String name);
  
  public BuildingEntity getBuilding(Long id);
  
  public void save(BuildingEntity building);
}
