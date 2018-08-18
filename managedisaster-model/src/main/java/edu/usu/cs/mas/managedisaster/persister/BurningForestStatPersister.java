package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningForestStatEntity;

public interface BurningForestStatPersister {
  
  public List<BurningForestStatEntity> getAllBurningForestStats();
  
  public List<BurningForestStatEntity> getBurningForestStat(ForestEntity forest);
  
  public void save(BurningForestStatEntity burningForestStat);
  
  public void cleanup();
}
