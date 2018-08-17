package edu.usu.cs.mas.managedisaster.display;

import sim.field.continuous.Continuous2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;

public interface TransportationLayout {
  /**
   * Set the positioning canvas for Transportation layout
   * @param positioningCanvas
   */
  public void setPositioningCanvas(Continuous2D positioningCanvas);
  
  /**
   * Returns the transportation layout 
   * @return
   */
  public ContinuousPortrayal2D getTransporationLayout();
  
  /**
   * Get the name of the transportation layer
   */
  public String getName();
  
}
