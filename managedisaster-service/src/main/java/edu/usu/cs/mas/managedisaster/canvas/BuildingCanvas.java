package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;

public interface BuildingCanvas {
  /**
   * Draw the building corresponds to the database building entity
   */
  public void drawBuildings();
  /**
   * Return the building portrayal to the main display frame 
   * @return
   */
  public FastValueLabeledGridPortrayal2D getBuildingsPortrayal();
  
  public void setBuildingsPortrayal(FastValueLabeledGridPortrayal2D buildingsPortrayal);
  /**
   * Return the name of the canvas
   * @return
   */
  public String getName();
  
  public void setBuildingsGrid(IntGrid2D buildingsGrid);
  
  public IntGrid2D getBuildingsGrid();
  
  public boolean isBuildingCoordinate(int x, int y);
  
  public int getBuildingId(int x, int y);
}
