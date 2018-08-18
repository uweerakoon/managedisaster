package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;

public interface ForestCanvas {
  /**
   * Draw the forest corresponds to the database forest entity
   */
  public void drawForests();
  /**
   * Return the forest portrayal to the main display frame 
   * @return
   */
  public FastValueLabeledGridPortrayal2D getForestsPortrayal();
  
  public void setForestsPortrayal(FastValueLabeledGridPortrayal2D forestsPortrayal);
  /**
   * Return the name of the canvas
   * @return
   */
  public String getName();
  
  public void setForestsGrid(IntGrid2D forestsGrid);
  
  public IntGrid2D getForestsGrid();
  
  public boolean isForestCoordinate(int x, int y);
  
  public int getForestId(int x, int y);
}
