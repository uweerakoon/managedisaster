package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;

public interface CoalitionForestPersister {

  public List<CoalitionForestEntity> getFeasibleCoalForests();
  
  public List<CoalitionForestEntity> getCoalitionForests(CoalitionEntity coalition);
  
  public List<CoalitionForestEntity> getUnallocatedCoalForestByCoalIds(List<Long> coalitionIds);
  
  public List<CoalitionForestEntity> getUnallocatedCoalForestByForestIds(List<Long> forestIds);
  
  public List<Integer> getCancelledCoalitionIds();
  
  public List<CoalitionForestEntity> getCoalitionForests(ForestEntity forest);
  
  public CoalitionForestEntity getAllocatedCoalForest(ForestEntity forest);
  
  public CoalitionForestEntity getAllocatedCoalForest(CoalitionEntity coalition);
  
  public CoalitionForestEntity getExecutingCoalForest(CoalitionEntity coalition);
  
  public List<CoalitionForestEntity> findBestUtilityCoalitionForests(ForestEntity forest);
  
  public CoalitionForestEntity getCoalitionForest(CoalitionEntity coalition, ForestEntity forest);
  
  public CoalitionForestEntity getFeasibleCoalForests(CoalitionEntity coalition, ForestEntity forest);
  
  public List<ForestEntity> getForests(CoalitionEntity coalition);
  
  public List<CoalitionEntity> getCoalitions(ForestEntity forest);
  
  public List<ForestEntity> getOneCoalitionForests();
  
  public void save(CoalitionForestEntity CoalitionForest);
  
  public void cancel(CoalitionForestEntity coalitionForest);
  
  public void delete(CoalitionForestEntity CoalitionForest);
  
  public void cleanup();
}
