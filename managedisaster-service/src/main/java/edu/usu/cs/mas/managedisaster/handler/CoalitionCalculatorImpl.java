package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.common.CoalitionForestStatus;
import edu.usu.cs.mas.managedisaster.common.UtilityAlgorithm;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.CoalitionForestPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlanner;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import sim.util.MutableInt2D;

public class CoalitionCalculatorImpl implements CoalitionCalculator {

  private static final Logger LOGGER = Logger.getLogger(CoalitionCalculatorImpl.class);
  private static final String UTILITY_ALGORITHM = "edu.usu.cs.mas.managedisaster.coalition.utility";

  @Inject
  private CoalitionForestPersister coalitionForestPersister;
  @Inject
  private FireCanvas fireCanvas;
  @Inject
  private ForestCanvas forestCanvas;
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
    List<ForestEntity> forests = coalitionForestPersister.getForests(coalition);
    if(CollectionUtils.isEmpty(forests)) {
      return 0.0;
    }
    for(ForestEntity forest : forests) {
      int minX = forest.getMinX();
      int maxX = forest.getMaxX();
      int minY = forest.getMinY();
      int maxY = forest.getMaxY();
      double[][] fire = fireCanvas.getCurrentFireGrid().field;
      double[][] smoke = fireCanvas.getCurrentSmokeGrid().field;
      for(int i = minX; i <= maxX; i++) {
        for(int j= minY; j <= maxY; j++) {
          if(!forestCanvas.isForestCoordinate(i, j)) {
            continue;
          }
          fireAmount += fire[i][j];
          fireAmount += smoke[i][j];
        }
      }
      CoalitionForestEntity coalForest = coalitionForestPersister.getCoalitionForest(coalition, forest);
      coalForest.setTaskAmount(fireAmount);
      coalitionForestPersister.save(coalForest);
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
    List<CoalitionForestEntity> coalForests = coalitionForestPersister.getCoalitionForests(coalition);
    for(CoalitionForestEntity coalForest : coalForests) {
      coalForest.setResourceAmount(waterAmount);
      coalitionForestPersister.save(coalForest);
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
    // 2. Calculate utility for all feasible coal-forest pairs
    List<CoalitionForestEntity> feabileCoalForests = coalitionForestPersister.getFeasibleCoalForests();
    for(CoalitionForestEntity feabileCoalForest : feabileCoalForests) {
      calculateUtility(feabileCoalForest);
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
  
  private void calculateUtility(CoalitionForestEntity coalForestEntity) {
    String algorithm = config.getString(UTILITY_ALGORITHM);
    UtilityAlgorithm utilityAlgorithm = UtilityAlgorithm.valueOf(algorithm);
    switch(utilityAlgorithm) {
      case ATV :
        setupATVUtility(coalForestEntity);
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
    // Calculate utility for feasible coalition's feasible coal-forest pairs
    List<ForestEntity> forests = coalitionForestPersister.getForests(coalition);
    for(ForestEntity forest : forests) {
      CoalitionForestEntity coalForestEntity = coalitionForestPersister.getFeasibleCoalForests(coalition, forest);
      if(coalForestEntity == null) {
        LOGGER.error("Not feasible coal-forest pair for feasible coalition: "+coalition.getId());
        continue;
      }
      MutableInt2D fireLocation = new MutableInt2D(forest.getFires().get(0).getX(), forest.getFires().get(0).getY());
      MutableInt2D closeRoadCoordination = routePlanner.findClosestRoadCoordinate(fireLocation, forest);
      // 1. Calculate the distance
      int distance = Math.abs(closeRoadCoordination.x - coalition.getX()) + Math.abs(closeRoadCoordination.y - coalition.getY());
      double utility = coalForestEntity.getResourceAmount() - coalForestEntity.getTaskAmount() - distance;
      coalForestEntity
          .withUtility(utility)
          .withAlgorithm(UtilityAlgorithm.ATV)
          .withStatus(CoalitionForestStatus.UTILIZED);
      coalitionForestPersister.save(coalForestEntity);
    }
  }
  
  private void setupATVUtility(CoalitionForestEntity coalForestEntity) {
    CoalitionEntity coalition = coalForestEntity.getCoalition();
    ForestEntity forest = coalForestEntity.getForest();
    FireStationEntity fireStation = coalition.getFireStation();
    
    int fsX = fireStation.getRoadX();
    int fsY = fireStation.getRoadY();
      
    double forestX = (forest.getMaxX() - forest.getMinX()) / 2;
    double forestY = (forest.getMaxY() - forest.getMinY()) / 2;
    // 1. Calculate the distance
    double distance = Math.sqrt(((forestX - fsX) * (forestX - fsX)) + ((forestY - fsY) * (forestY - fsY)));
    double utility = coalForestEntity.getResourceAmount() - coalForestEntity.getTaskAmount() - distance;
    
    coalForestEntity
        .withUtility(utility)
        .withAlgorithm(UtilityAlgorithm.ATV)
        .withStatus(CoalitionForestStatus.UTILIZED);
    
    coalitionForestPersister.save(coalForestEntity);
  }
}
