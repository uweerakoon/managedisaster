package edu.usu.cs.mas.managedisaster.canvas;

import sim.field.grid.IntGrid2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.util.MutableInt2D;

public interface RoadCanvas {
  
  /**
   * Draws the roads
   */
  public void drawRoads();
  
  /**
   * Set the road portrayal
   * @param roadPortrayal
   */
  public void setRoadPortrayal(FastValueGridPortrayal2D roadPortrayal);
  
  /**
   * Get the road portrayal
   * @return
   */
  public FastValueGridPortrayal2D getRoadPortrayal();
  
  /**
   * Get the road grid
   * @return
   */
  public IntGrid2D getRoadGrid();
  
  /**
   * Set the road grid
   * @param roadGrid
   */
  public void setRoadGrid(IntGrid2D roadGrid);
  
  /**
   * Get the name for the road map
   * @return
   */
  public String getName();
  /**
   * Check whether the given coordinates has a road
   */
  public boolean isRoadCoordinate(int x, int y);
  
  public int getRoadId(MutableInt2D location);
}
