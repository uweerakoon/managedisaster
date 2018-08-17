package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionStatEntity;

public interface CoalitionStatPersister {
	
  public List<CoalitionStatEntity> getAllCoalitionStats();
  
  public List<CoalitionStatEntity> getCoalitionStat(CoalitionEntity coalition);
  
  public void save(CoalitionStatEntity coalitionStat);
  
  public void cleanup();
}
