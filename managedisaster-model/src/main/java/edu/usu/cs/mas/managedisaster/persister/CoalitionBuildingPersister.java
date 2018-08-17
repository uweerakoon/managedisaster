package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;

public interface CoalitionBuildingPersister {

  public List<CoalitionBuildingEntity> getFeasibleCoalBuilds();
  
  public List<CoalitionBuildingEntity> getCoalitionBuildings(CoalitionEntity coalition);
  
  public List<CoalitionBuildingEntity> getUnallocatedCoalBuildByCoalIds(List<Long> coalitionIds);
  
  public List<CoalitionBuildingEntity> getUnallocatedCoalBuildByBuildIds(List<Long> buildIds);
  
  public List<Integer> getCancelledCoalitionIds();
  
  public List<CoalitionBuildingEntity> getCoalitionBuildings(BuildingEntity building);
  
  public CoalitionBuildingEntity getAllocatedCoalBuild(BuildingEntity building);
  
  public CoalitionBuildingEntity getAllocatedCoalBuild(CoalitionEntity coalition);
  
  public CoalitionBuildingEntity getExecutingCoalBuild(CoalitionEntity coalition);
  
  public List<CoalitionBuildingEntity> findBestUtilityCoalitionBuildings(BuildingEntity building);
  
  public CoalitionBuildingEntity getCoalitionBuilding(CoalitionEntity coalition, BuildingEntity building);
  
  public CoalitionBuildingEntity getFeasibleCoalBuilds(CoalitionEntity coalition, BuildingEntity building);
  
  public List<BuildingEntity> getBuildings(CoalitionEntity coalition);
  
  public List<CoalitionEntity> getCoalitions(BuildingEntity building);
  
  public List<BuildingEntity> getOneCoalitionBuildings();
  
  public void save(CoalitionBuildingEntity CoalitionBuilding);
  
  public void cancel(CoalitionBuildingEntity coalitionBuilding);
  
  public void delete(CoalitionBuildingEntity CoalitionBuilding);
  
  public void cleanup();
}
