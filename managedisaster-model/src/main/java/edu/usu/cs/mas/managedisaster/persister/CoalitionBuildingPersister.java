package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;

public interface CoalitionBuildingPersister {

  public List<CoalitionForestEntity> getFeasibleCoalBuilds();
  
  public List<CoalitionForestEntity> getCoalitionBuildings(CoalitionEntity coalition);
  
  public List<CoalitionForestEntity> getUnallocatedCoalBuildByCoalIds(List<Long> coalitionIds);
  
  public List<CoalitionForestEntity> getUnallocatedCoalBuildByBuildIds(List<Long> buildIds);
  
  public List<Integer> getCancelledCoalitionIds();
  
  public List<CoalitionForestEntity> getCoalitionBuildings(ForestEntity building);
  
  public CoalitionForestEntity getAllocatedCoalBuild(ForestEntity building);
  
  public CoalitionForestEntity getAllocatedCoalBuild(CoalitionEntity coalition);
  
  public CoalitionForestEntity getExecutingCoalBuild(CoalitionEntity coalition);
  
  public List<CoalitionForestEntity> findBestUtilityCoalitionBuildings(ForestEntity building);
  
  public CoalitionForestEntity getCoalitionBuilding(CoalitionEntity coalition, ForestEntity building);
  
  public CoalitionForestEntity getFeasibleCoalBuilds(CoalitionEntity coalition, ForestEntity building);
  
  public List<ForestEntity> getBuildings(CoalitionEntity coalition);
  
  public List<CoalitionEntity> getCoalitions(ForestEntity building);
  
  public List<ForestEntity> getOneCoalitionBuildings();
  
  public void save(CoalitionForestEntity CoalitionBuilding);
  
  public void cancel(CoalitionForestEntity coalitionBuilding);
  
  public void delete(CoalitionForestEntity CoalitionBuilding);
  
  public void cleanup();
}
