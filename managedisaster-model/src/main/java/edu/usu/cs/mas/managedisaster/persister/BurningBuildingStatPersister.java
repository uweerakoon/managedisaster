package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningForestStatEntity;

public interface BurningBuildingStatPersister {
  
  public List<BurningForestStatEntity> getAllBurningBuildingStats();
  
  public List<BurningForestStatEntity> getBurningBuildingStat(ForestEntity building);
  
  public void save(BurningForestStatEntity burningBuildingStat);
  
  public void cleanup();
}
