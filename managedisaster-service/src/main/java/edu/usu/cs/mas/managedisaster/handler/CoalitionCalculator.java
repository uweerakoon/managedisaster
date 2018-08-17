package edu.usu.cs.mas.managedisaster.handler;

import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;

public interface CoalitionCalculator {

  public double getFireAndSmokeAmount(CoalitionEntity coalition);
  
  public double getWaterAmount(CoalitionEntity coalition);
  
  public void calculateUtility();
}
