package edu.usu.cs.mas.managedisaster.handler;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import sim.field.grid.DoubleGrid2D;
import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

import com.google.common.base.Preconditions;

import edu.usu.cs.mas.managedisaster.Simulator;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.exception.ManageDisasterServiceException;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class ExtinguisherImpl implements Extinguisher {
  
  private static final Logger LOGGER = Logger.getLogger(ExtinguisherImpl.class);
  
  private static final int INITIAL_VALUE = 0;
  private static final int INCREMENTAL_RADIUS_VALUE = 1;
  private static final int INIT_RADIUS = -1;
  
  private DoubleGrid2D currentWaterGrid = new DoubleGrid2D(Simulator.GRID_WIDTH, Simulator.GRID_HEIGHT, 0);
  private DoubleGrid2D newWaterGrid = new DoubleGrid2D(Simulator.GRID_WIDTH, Simulator.GRID_HEIGHT, 0);
  
  @Inject
  private FireCanvas fireCanvas;
  @Inject
  private ForestCanvas forestCanvas;
  
  public ExtinguisherImpl() { }
  
  public ExtinguisherImpl(FireCanvas fireCanvas, ForestCanvas forestCanvas, 
      DoubleGrid2D currentWaterGrid, DoubleGrid2D newWaterGrid) {
    this.fireCanvas = fireCanvas;
    this.forestCanvas = forestCanvas;
    this.currentWaterGrid = currentWaterGrid;
    this.newWaterGrid = newWaterGrid;
  }
  
  @Override
  public void extinguish(FireEntity fire, AgentPlayer agent) {
    ForestEntity forest = fire.getBurningForest();
    
    checkInitValues(agent, forest);
    
    initNewValues();
    
    try {
      if(agent.getWaterImpactCenter() == null) {
        MutableInt2D newWaterImpactCenter = getWaterImpactCenter(fire, agent);
        agent.setWaterImpactCenter(newWaterImpactCenter);
        agent.setWaterImpactRadius(INITIAL_VALUE);
      }
      
      // Need to get some water in order the diffuse the water
      if(agent.getWaterImpactRadius() == INITIAL_VALUE // initial radius
          || currentWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y] == 0) {
        setupInitialWater(agent, fire);
        // Has some water to start diffuse
        if(newWaterGrid.field[agent.getWaterImpactCenter().x][agent.getWaterImpactCenter().y] > 0) { 
          agent.setWaterImpactRadius(agent.getWaterImpactRadius() + INCREMENTAL_RADIUS_VALUE); // start diffuse water
        }
      }
      else { 
        fight(forest, agent, fire);
        agent.setWaterImpactRadius(agent.getWaterImpactRadius() + INCREMENTAL_RADIUS_VALUE);
      }
      
      copyCurrentValues();
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public boolean isFireExtinguised(FireEntity fire) {
    if(fire.isExtinguished()) {
      return true;
    }
    ForestEntity forest = fire.getBurningForest();
    boolean isForestBurning = fireCanvas.isForestBurning(forest);
    if(!isForestBurning) {
      fire.setExtinguished(true);
    }
    return !isForestBurning;
  }
  
  @Override
  public void releaseAgent(AgentPlayer agent) {
    if(agent.getFire() == null) {
      return;
    }
    LOGGER.info("Releasing the agent from the task being assigned Agent Id: "+agent.getId()+" coordinates: "+agent.getCurrentLocation());
    agent.setWaterImpactCenter(null);
    agent.setWaterImpactRadius(INIT_RADIUS);
    agent.setFire(null);
  }

  private void checkInitValues(AgentPlayer agent, ForestEntity forest) {
    Preconditions.checkNotNull(forest, "Buring forest cannot be null");
    Preconditions.checkNotNull(agent.getChemical(), "Agent has not chemical type to use");
    Preconditions.checkArgument(agent.getChemicalAmount() > 0, "Agent has not chemical amount to use");
  }
  
  private void initNewValues() {
    fireCanvas.getNewFireGrid().setTo(fireCanvas.getCurrentFireGrid());
    fireCanvas.getNewSmokeGrid().setTo(fireCanvas.getCurrentSmokeGrid());
    newWaterGrid.setTo(currentWaterGrid);
  }
  
  private void copyCurrentValues() {
    fireCanvas.getCurrentFireGrid().setTo(fireCanvas.getNewFireGrid());
    fireCanvas.getCurrentSmokeGrid().setTo(fireCanvas.getNewSmokeGrid());
    currentWaterGrid.setTo(newWaterGrid);
  }
  
  private void fight(ForestEntity forest, AgentPlayer agent, FireEntity fire) {
    int minX = forest.getMinX(), maxX = forest.getMaxX();
    int minY = forest.getMinY(), maxY = forest.getMaxY();
    
    int startX, endX;
    
    double[][] currentWaterGridField = currentWaterGrid.field;
    int[][] forestGridField = forestCanvas.getForestsGrid().field;
    
    double[] previousWaterXAxis, currentWaterXAxis, nextWaterXAxis = null;
    int[] previousForestXAxis, currentForestXAxis, nextForestXAxis = null;
    
    startX = agent.getWaterImpactCenter().x - agent.getWaterImpactRadius();
    if(startX < minX) {
      startX = minX;
    }
    
    endX = agent.getWaterImpactCenter().x + agent.getWaterImpactRadius();
    if(endX > maxX ) {
      endX = maxX;
    }
    
    previousWaterXAxis = currentWaterGridField[startX];
    currentWaterXAxis = currentWaterGridField[startX];
    previousForestXAxis = forestGridField[startX];
    currentForestXAxis = forestGridField[startX];
    
    diffuseWaterInForest(agent, startX, endX, minY, maxY, fire,
      previousForestXAxis, currentForestXAxis, nextForestXAxis, 
      previousWaterXAxis, currentWaterXAxis, nextWaterXAxis, 
      forestGridField, currentWaterGridField);
  }
  
  private void diffuseWaterInForest(AgentPlayer agent, int startX, int endX, int minY, int maxY, FireEntity fire, 
      int[] previousForestXAxis, int[] currentForestXAxis, int[] nextForestXAxis,
      double[] previousWaterXAxis, double[] currentWaterXAxis, double[] nextWaterXAxis, 
      int[][] forestGridField, double[][] currentWaterGridField) {
    
    Chemical chemical = agent.getChemical();
    double chemicalEvaporationRate = chemical.getEvaporationRate();
    long extinguisherCoefficient = chemical.getExtinguishingCoefficient();
    
    double availableChemicalAmt = getChemicalAmount(agent);
    
    for(int x = startX; x <= endX; x++) {
      int xPlus1 = x + 1 < endX ? x + 1 : endX;
      nextForestXAxis = forestGridField[xPlus1];
      nextWaterXAxis = currentWaterGridField[xPlus1];
      int yMinus1 = minY;
      
      for(int y = minY; y <= maxY; y++) {
        if(availableChemicalAmt <= 0) {
          return;
        }
        if(currentForestXAxis[y] == 0) {
          yMinus1 = y;
          continue;
        }
        
        int yPlus1 = y+1 > maxY ? maxY : y+1;
        
        double average = 0.0;
        if(currentWaterXAxis[y] > 0) {
          average = calculateAverage(y, yMinus1, yPlus1, 
                      previousWaterXAxis, currentWaterXAxis, nextWaterXAxis, 
                      previousForestXAxis, currentForestXAxis, nextForestXAxis);
        }

        average = (average + previousWaterXAxis[y]
            + currentWaterXAxis[yMinus1] + currentWaterXAxis[y] + currentWaterXAxis[yPlus1]
            + nextWaterXAxis[y]) / 5.0;
        
        double newValue = 0.0;
        if(average > 0) {
          newValue = (1.0 - chemicalEvaporationRate) * average;
          if(newValue > availableChemicalAmt) {
            newValue = availableChemicalAmt;
            availableChemicalAmt = 0.0;
          }
          availableChemicalAmt -= newValue;
        }
        
        if(newValue > 0) {
          newValue = adjustCells(agent, x, y, newValue, fire);
        }
        
        if(newValue > 0) {
          availableChemicalAmt += newValue;
        }
        
        yMinus1 = y;
      }
      
      previousWaterXAxis = currentWaterXAxis;
      currentWaterXAxis = nextWaterXAxis;
      
      previousForestXAxis = currentForestXAxis;
      currentForestXAxis = nextForestXAxis;
    }
    
    if(availableChemicalAmt > 0) {
      agent.setChemicalAmount(agent.getChemicalAmount() + (availableChemicalAmt / extinguisherCoefficient));
    }
  }
  
  private double calculateAverage(int y, int yMinus1, int yPlus1, 
      double[] previousWaterXAxis, double[] currentWaterXAxis, double[] nextWaterXAxis, 
      int[] previousForestXAxis, int[] currentForestXAxis, int[] nextForestXAxis) {
    double average = 0.0;
    double currentValue = currentWaterXAxis[y];
    
    if(currentForestXAxis[yMinus1] == 0 || currentWaterXAxis[yMinus1] == 0.0) { // x, y-1
      average += currentValue;
    }
    if(currentForestXAxis[yPlus1] == 0 || currentWaterXAxis[yPlus1] == 0.0) { // x, y+1
      average += currentValue;
    }
    if(previousForestXAxis[y] == 0 || previousWaterXAxis[y] == 0.0) { // x-1, y
      average += currentValue;
    }
    if(nextForestXAxis[y] == 0 || nextWaterXAxis[y] == 0.0) { // x+1, y
      average += currentValue;
    }
    
    return average;
  }
  
  private double adjustCells(AgentPlayer agent, int x, int y, double newValue, FireEntity fire) {
    if(newValue > 0) {
      newValue = changeFireAmount(x, y, newValue, fire);
    }
    if(newValue > 0) {
      newValue = changeSmokeAmount(x, y, newValue, fire);
    }
    if(newValue > 0) {
      newValue = changeWaterAmount(x, y, agent.getChemical(), newValue, fire);
    }
    
    return newValue;
  }

  private MutableInt2D getWaterImpactCenter(FireEntity fire, AgentPlayer agent) {
    int y = agent.getY();
    int x = agent.getX();
    double vicinity = agent.getMinimumFireProximity();
    
    int fireX = fire.getX();
    int fireY = fire.getY();
    
    MutableInt2D newWaterImpactCenter = null;
    
    double agentFireDistance = Math.sqrt(((x - fireX) * (x - fireX))
                                        +((y - fireY) * (y - fireY)));
    
    if(vicinity >= agentFireDistance) {
      newWaterImpactCenter = new MutableInt2D(fireX, fireY);
    }
    else {
      MutableDouble2D agentLocation = new MutableDouble2D(x, y);
      MutableDouble2D fireLocation = new MutableDouble2D(fireX, fireY);
      MutableDouble2D directionVector = new MutableDouble2D(agentLocation.getX(), agentLocation.getY());
      MutableDouble2D waterImpactCenter = agentLocation;
      directionVector.subtract(fireLocation, agentLocation);
      directionVector.resize(vicinity);
      
      if(directionVector.length() > vicinity) {
        directionVector.resize(vicinity);
      }
      
      waterImpactCenter.add(agentLocation, directionVector);
      newWaterImpactCenter = new MutableInt2D((int)waterImpactCenter.x, (int)waterImpactCenter.y);
    }
    
    if(forestCanvas.getForestsGrid().field[newWaterImpactCenter.x][newWaterImpactCenter.y] == 0) {
      LOGGER.fatal("The agent is not close to the buring forest. Agent: "+agent+" Fire: "+fire);
      throw new ManageDisasterServiceException("The agent is not close to the buring forest. Agent: "+agent+" Fire: "+fire);
    }
    
    return newWaterImpactCenter;
  }
  
  private void setupInitialWater(AgentPlayer agent, FireEntity fire) {
    long extinguisherCoefficient = agent.getChemical().getExtinguishingCoefficient();
    int x = agent.getWaterImpactCenter().x;
    int y = agent.getWaterImpactCenter().y;
    double newValue = getChemicalAmount(agent);
    
    newValue = adjustCells(agent, x, y, newValue, fire);
    
    if(newValue > 0) {
      agent.setChemicalAmount(agent.getChemicalAmount() + (newValue / extinguisherCoefficient));
    }
  }
  
  private double changeFireAmount(int x, int y, double newValue, FireEntity fire) {
    double[] currentFireXAxis = fireCanvas.getCurrentFireGrid().field[x];
    double[] newFireXAxis = fireCanvas.getNewFireGrid().field[x];
    
    if(newValue > 0 && currentFireXAxis[y] > 0) {
      double currentFireAmount = currentFireXAxis[y];
      if(newValue > currentFireAmount) {
        newFireXAxis[y] = 0;
        newValue -= currentFireAmount;
        fire.setCurrentFireValue(fire.getCurrentFireValue() - currentFireAmount);
      }
      else {
        newFireXAxis[y] = currentFireAmount - newValue;
        newValue = 0.0;
        fire.setCurrentFireValue(fire.getCurrentFireValue() - (currentFireAmount - newValue));
      }
    }
    return newValue;
  }
  
  private double changeSmokeAmount(int x, int y, double newValue, FireEntity fire) {
    double[] currentSmokeXAxis = fireCanvas.getCurrentSmokeGrid().field[x];
    double[] newSmokeXAxis = fireCanvas.getNewSmokeGrid().field[x];
    
    if(newValue > 0 && currentSmokeXAxis[y] > 0) {
      double currentSmokeAmount = currentSmokeXAxis[y];
      if(newValue > currentSmokeAmount) { // has enough chemical to put down smoke
        newSmokeXAxis[y] = 0;
        newValue -= currentSmokeAmount;
        fire.setCurrentSmokeValue(fire.getCurrentSmokeValue() - currentSmokeAmount);
      }
      else { // does not have enough chemicals to put down the smoke
        newSmokeXAxis[y] = currentSmokeAmount - newValue;
        newValue = 0.0;
        fire.setCurrentSmokeValue(fire.getCurrentSmokeValue() - (currentSmokeAmount - newValue));
      }
    }
    return newValue;
  }
  
  private double changeWaterAmount(int x, int y, Chemical chemical, double newValue, FireEntity fire) {
    double[] currentWaterXAxis = currentWaterGrid.field[x];
    double[] newWaterXAxis = newWaterGrid.field[x];
    double maximumChemicalAmount = chemical.getMaximumAmount();
    
    if(newValue > 0 && currentWaterXAxis[y] < maximumChemicalAmount) {
      newWaterXAxis[y] = currentWaterXAxis[y] + newValue;
      if(newWaterXAxis[y] > maximumChemicalAmount) {
        newWaterXAxis[y] = maximumChemicalAmount;
        newValue -= (maximumChemicalAmount - currentWaterXAxis[y]);
      }
      else {
        newValue = 0.0;
      }
    }
    return newValue;
  }
  
  private double getChemicalAmount(AgentPlayer agent) {
    Chemical chemical = agent.getChemical();
    double chemicalEvaporationRate = chemical.getEvaporationRate();
    long extinguisherCoefficient = chemical.getExtinguishingCoefficient();
    int chemicalAmount = agent.getSquirtPressure();
    double newValue = 0.0;
    
    newValue = (1.0 - chemicalEvaporationRate) * chemicalAmount;
    
    if(agent.getChemicalAmount() >= newValue) {
      agent.setChemicalAmount(agent.getChemicalAmount() -  newValue);
    }
    else if(agent.getChemicalAmount() > 0) {
      newValue = agent.getChemicalAmount();
      agent.setChemicalAmount(0.0);
    }
    else {
      LOGGER.fatal("Agent id: "+agent.getId()+" coordinates: "+agent.getCurrentLocation()+" has no "+chemical+" at the initiation time. Agent: "+agent);
      throw new ManageDisasterServiceException("Agent id: "+agent.getId()+" coordinates: "+agent.getCurrentLocation()+" has no "+chemical+" at the initiation time. Agent: "+agent);
    }
    
    newValue *= extinguisherCoefficient;
    return newValue;
  }

  public DoubleGrid2D getCurrentWaterGrid() {
    return currentWaterGrid;
  }

  public DoubleGrid2D getNewWaterGrid() {
    return newWaterGrid;
  }

  public void setNewWaterGrid(DoubleGrid2D newWaterGrid) {
    this.newWaterGrid = newWaterGrid;
  }

  public void setCurrentWaterGrid(DoubleGrid2D currentWaterGrid) {
    this.currentWaterGrid = currentWaterGrid;
  }
}
