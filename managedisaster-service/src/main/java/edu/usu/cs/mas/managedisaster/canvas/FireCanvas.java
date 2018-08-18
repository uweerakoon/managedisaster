package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import sim.field.grid.DoubleGrid2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;

public interface FireCanvas {
  
  public DoubleGrid2D getCurrentFireGrid();
  
  public void setCurrentFireGrid(DoubleGrid2D currentFireGrid);
  
  public DoubleGrid2D getNewFireGrid();
  
  public void setNewFireGrid(DoubleGrid2D newFireGrid);

  public void setFirePortrayal(FastValueGridPortrayal2D firePortrayal);
  
  public FastValueGridPortrayal2D getSmokePortrayal();
  
  public void setSmokePortrayal(FastValueGridPortrayal2D smokePortrayal);
  
  public DoubleGrid2D getCurrentSmokeGrid();
  
  public void setCurrentSmokeGrid(DoubleGrid2D currentSmokeGrid);
  
  public DoubleGrid2D getNewSmokeGrid();
  
  public void setNewSmokeGrid(DoubleGrid2D newSmokeGrid);
  
  public boolean isForestBurning(ForestEntity forest);
}
