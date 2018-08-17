package edu.usu.cs.mas.managedisaster.common;

public enum Chemical {
  
  CARBONDIOXIDE(5000, 0.005, 20),
  WATER(1000, 0.02, 50),
  HALON(10000, 0.001, 10);

  private long extinguishingCoefficient;
  private double evaporationRate;
  private double maximumAmount;
  
  Chemical(long extinguishingCoefficient, double evaporationRate, double maximumAmount) {
    this.extinguishingCoefficient = extinguishingCoefficient;
    this.evaporationRate = evaporationRate;
    this.maximumAmount = maximumAmount;
  }
  
  public long getExtinguishingCoefficient() {
    return extinguishingCoefficient;
  }

  public double getEvaporationRate() {
    return evaporationRate;
  }

  public double getMaximumAmount() {
    return maximumAmount;
  }
}
