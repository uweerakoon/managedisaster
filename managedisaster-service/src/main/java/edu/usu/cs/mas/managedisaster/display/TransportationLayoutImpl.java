package edu.usu.cs.mas.managedisaster.display;

import javax.inject.Inject;

import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import sim.field.continuous.Continuous2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;

public class TransportationLayoutImpl implements TransportationLayout{
  
  @Inject
  private AgentSociety agentSociety;
  
  private final ContinuousPortrayal2D transportationLayout = new ContinuousPortrayal2D();
  
  private static final String NAME = "Transportation Layer";
  
  /*private static final double SCALE = 1.0;
  
  private static final boolean FILLED = true;*/
  
  /*@Inject
  public TransportationLayoutImpl(AgentSociety agentSociety){
    this.agentSociety = agentSociety;
  }*/

  
  @Override
  public void setPositioningCanvas(Continuous2D positioningCanvas){
    transportationLayout.setField(positioningCanvas);
  }
  
  @Override
  public ContinuousPortrayal2D getTransporationLayout(){
    return transportationLayout;
  }
  
  @Override
  public String getName(){
    return NAME;
  }
}
