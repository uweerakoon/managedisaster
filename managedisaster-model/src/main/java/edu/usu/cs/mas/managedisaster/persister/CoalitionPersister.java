package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;

public interface CoalitionPersister {

  public CoalitionEntity getCoalition(Long id);
  
	public List<CoalitionEntity> getAllCoalitions();
	
	public List<CoalitionEntity> getActiveCoalitions();
	
	public List<CoalitionEntity> getFormingCoalitions();
	
	public List<CoalitionEntity> getOptimizingCoalitions();

	public List<CoalitionEntity> getFeasibleCoalitions();
	
	public List<CoalitionEntity> getFeasibleUnallocatedCoalitions();
	
	public List<CoalitionEntity> getUnallocatedCoalitions(FireStationEntity fireStation);
	
	public void save(CoalitionEntity coalition);
	
	public void cancel(CoalitionEntity coalition);
	
	public void delete(CoalitionEntity coalition);
	
	public void cleanup(); 
}
