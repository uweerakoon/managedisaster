package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.canvas.BuildingCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.common.CoalitionBuildingStatus;
import edu.usu.cs.mas.managedisaster.common.UtilityAlgorithm;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlanner;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import sim.util.MutableInt2D;

public class CoalitionCalculatorImpl implements CoalitionCalculator {

  private static final Logger LOGGER = Logger.getLogger(CoalitionCalculatorImpl.class);
  private static final String UTILITY_ALGORITHM = "edu.usu.cs.mas.managedisaster.coalition.utility";

  @Inject
  private CoalitionBuildingPersister coalitionBuildingPersister;
  @Inject
  private FireCanvas fireCanvas;
  @Inject
  private BuildingCanvas buildingCanvas;
  @Inject
  private AgentSociety agentSociety;
  @Inject
  private CoalitionPersister coalitionPersister;
  @Inject
  private Config config;
  @Inject
  private RoutePlanner routePlanner;

  @Override
  public double getFireAndSmokeAmount(CoalitionEntity coalition) {
    double fireAmount = 0.0;
    List<BuildingEntity> buildings = coalitionBuildingPersister.getBuildings(coalition);
    if(CollectionUtils.isEmpty(buildings)) {
      return 0.0;
    }
    for(BuildingEntity building : buildings) {
      int minX = building.getMinX();
      int maxX = building.getMaxX();
      int minY = building.getMinY();
      int maxY = building.getMaxY();
      double[][] fire = fireCanvas.getCurrentFireGrid().field;
      double[][] smoke = fireCanvas.getCurrentSmokeGrid().field;
      for(int i = minX; i <= maxX; i++) {
        for(int j= minY; j <= maxY; j++) {
          if(!buildingCanvas.isBuildingCoordinate(i, j)) {
            continue;
          }
          fireAmount += fire[i][j];
          fireAmount += smoke[i][j];
        }
      }
      CoalitionBuildingEntity coalBuild = coalitionBuildingPersister.getCoalitionBuilding(coalition, building);
      coalBuild.setTaskAmount(fireAmount);
      coalitionBuildingPersister.save(coalBuild);
    }
    return fireAmount;
  }

  @Override
  public double getWaterAmount(CoalitionEntity coalition) {
    double waterAmount = 0.0;
    List<AgentEntity> agents = coalition.getAgents();
    if(CollectionUtils.isEmpty(agents)) {
      return 0.0;
    }
    for(AgentEntity agent : agents) {
      AgentPlayer player = agentSociety.getAgent(agent.getId());
      Chemical chemical = player.getChemical();
      double chemicalCoefficient = chemical.getExtinguishingCoefficient();
      double chemicalAmount = player.getChemicalAmount();
      waterAmount += (chemicalAmount * chemicalCoefficient);
    }
    List<CoalitionBuildingEntity> coalBuilds = coalitionBuildingPersister.getCoalitionBuildings(coalition);
    for(CoalitionBuildingEntity coalBuild : coalBuilds) {
      coalBuild.setResourceAmount(waterAmount);
      coalitionBuildingPersister.save(coalBuild);
    }
    return waterAmount;
  }

  @Override
  public void calculateUtility() {
    List<CoalitionEntity> feasibleCoalitions = coalitionPersister.getFeasibleCoalitions();
    // 1. Calculate the utility of all the feasible coalitions
    for(CoalitionEntity enabledCoalition : feasibleCoalitions) {
      calculateUtility(enabledCoalition);
    }
    // 2. Calculate utility for all feasible coal-build pairs
    List<CoalitionBuildingEntity> feabileCoalBuilds = coalitionBuildingPersister.getFeasibleCoalBuilds();
    for(CoalitionBuildingEntity feabileCoalBuild : feabileCoalBuilds) {
      calculateUtility(feabileCoalBuild);
    }
  }

  private double calculateUtility(CoalitionEntity coalition) {
    double utility = 0.0;
    String algorithm = config.getString(UTILITY_ALGORITHM);
    UtilityAlgorithm utilityAlgorithm = UtilityAlgorithm.valueOf(algorithm);
    switch(utilityAlgorithm) {
      case ATV :
        setupATVUtility(coalition);
        break;
      case NECTAR:
        utility = calculateNectarUtility(coalition);
        break;
      default:
        LOGGER.error("The Utility Algorithm is not defined yet: edu.usu.cs.mas.managedisaster.coalition.utility");
    }
    return utility;
  }
  
  private void calculateUtility(CoalitionBuildingEntity coalBuildEntity) {
    String algorithm = config.getString(UTILITY_ALGORITHM);
    UtilityAlgorithm utilityAlgorithm = UtilityAlgorithm.valueOf(algorithm);
    switch(utilityAlgorithm) {
      case ATV :
        setupATVUtility(coalBuildEntity);
        break;
      case NECTAR:
        
        break;
      default:
        LOGGER.error("The Utility Algorithm is not defined yet: edu.usu.cs.mas.managedisaster.coalition.utility");
    }
  }

  private double calculateNectarUtility(CoalitionEntity coalition) {
    // TODO Auto-generated method stub
    return 0;
  }

  private void setupATVUtility(CoalitionEntity coalition) {
    // Calculate utility for feasible coalition's feasible coal-build pairs
    List<BuildingEntity> buildings = coalitionBuildingPersister.getBuildings(coalition);
    for(BuildingEntity building : buildings) {
      CoalitionBuildingEntity coalBuildEntity = coalitionBuildingPersister.getFeasibleCoalBuilds(coalition, building);
      if(coalBuildEntity == null) {
        LOGGER.error("Not feasible coal-build pair for feasible coalition: "+coalition.getId());
        continue;
      }
      MutableInt2D fireLocation = new MutableInt2D(building.getFires().get(0).getX(), building.getFires().get(0).getY());
      MutableInt2D closeRoadCoordination = routePlanner.findClosestRoadCoordinate(fireLocation, building);
      // 1. Calculate the distance
      int distance = Math.abs(closeRoadCoordination.x - coalition.getX()) + Math.abs(closeRoadCoordination.y - coalition.getY());
      double utility = coalBuildEntity.getResourceAmount() - coalBuildEntity.getTaskAmount() - distance;
      coalBuildEntity
          .withUtility(utility)
          .withAlgorithm(UtilityAlgorithm.ATV)
          .withStatus(CoalitionBuildingStatus.UTILIZED);
      coalitionBuildingPersister.save(coalBuildEntity);
    }
  }
  
  private void setupATVUtility(CoalitionBuildingEntity coalBuildEntity) {
    CoalitionEntity coalition = coalBuildEntity.getCoalition();
    BuildingEntity building = coalBuildEntity.getBuilding();
    FireStationEntity fireStation = coalition.getFireStation();
    
    int fsX = fireStation.getRoadX();
    int fsY = fireStation.getRoadY();
      
    double buildX = (building.getMaxX() - building.getMinX()) / 2;
    double buildY = (building.getMaxY() - building.getMinY()) / 2;
    // 1. Calculate the distance
    double distance = Math.sqrt(((buildX - fsX) * (buildX - fsX)) + ((buildY - fsY) * (buildY - fsY)));
    double utility = coalBuildEntity.getResourceAmount() - coalBuildEntity.getTaskAmount() - distance;
    
    coalBuildEntity
        .withUtility(utility)
        .withAlgorithm(UtilityAlgorithm.ATV)
        .withStatus(CoalitionBuildingStatus.UTILIZED);
    
    coalitionBuildingPersister.save(coalBuildEntity);
  }
}
