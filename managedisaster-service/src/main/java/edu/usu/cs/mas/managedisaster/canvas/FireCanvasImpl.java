package edu.usu.cs.mas.managedisaster.canvas;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import sim.field.grid.DoubleGrid2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;

public class FireCanvasImpl implements FireCanvas {

  public static final double MAX_HEAT = 10000;
  public static final double MIN_HEAT = 6000;
  public static final MersenneTwisterFast RANDOM = new MersenneTwisterFast(System.currentTimeMillis());
  
  private final String name = "Fire";
  
  private FastValueGridPortrayal2D firePortrayal = new FastValueGridPortrayal2D("Fire");
  private DoubleGrid2D currentFireGrid;
  private DoubleGrid2D newFireGrid;
  
  private FastValueGridPortrayal2D smokePortrayal = new FastValueGridPortrayal2D("Smoke");
  private DoubleGrid2D currentSmokeGrid;
  private DoubleGrid2D newSmokeGrid;
  
  public FastValueGridPortrayal2D getFirePortrayal() {
    return firePortrayal;
  }
  
  @Override
  public void setFirePortrayal(FastValueGridPortrayal2D firePortrayal) {
    this.firePortrayal = firePortrayal;
  }
  
  @Override
  public DoubleGrid2D getCurrentFireGrid() {
    return currentFireGrid;
  }
  
  @Override
  public void setCurrentFireGrid(DoubleGrid2D currentFireGrid) {
    this.currentFireGrid = currentFireGrid;
  }
  
  
  @Override
  public DoubleGrid2D getNewFireGrid() {
    return newFireGrid;
  }
  
  @Override
  public void setNewFireGrid(DoubleGrid2D newFireGrid) {
    this.newFireGrid = newFireGrid;
  }
  
  @Override
  public FastValueGridPortrayal2D getSmokePortrayal() {
    return smokePortrayal;
  }

  @Override
  public void setSmokePortrayal(FastValueGridPortrayal2D smokePortrayal) {
    this.smokePortrayal = smokePortrayal;
  }

  @Override
  public DoubleGrid2D getCurrentSmokeGrid() {
    return currentSmokeGrid;
  }

  @Override
  public void setCurrentSmokeGrid(DoubleGrid2D currentSmokeGrid) {
    this.currentSmokeGrid = currentSmokeGrid;
  }

  @Override
  public DoubleGrid2D getNewSmokeGrid() {
    return newSmokeGrid;
  }

  @Override
  public void setNewSmokeGrid(DoubleGrid2D newSmokeGrid) {
    this.newSmokeGrid = newSmokeGrid;
  }
  
  @Override
  public boolean isBuildingBurning(ForestEntity building) {
    int minX = building.getMinX(), maxX = building.getMaxX();
    int minY = building.getMinY(), maxY = building.getMaxY();
    
    for(int i = minX; i <= maxX; i++) {
      for(int j = minY; j <= maxY; j++) {
        if(currentSmokeGrid.field[i][j] > 0 || currentFireGrid.field[i][j] > 0) {
          return true;
        }
      }
    }
    
    return false;
  }

  public String getName() {
    return name;
  }
  
}
