package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningBuildingStatEntity;

public interface BurningBuildingStatPersister {
  
  public List<BurningBuildingStatEntity> getAllBurningBuildingStats();
  
  public List<BurningBuildingStatEntity> getBurningBuildingStat(BuildingEntity building);
  
  public void save(BurningBuildingStatEntity burningBuildingStat);
  
  public void cleanup();
}
