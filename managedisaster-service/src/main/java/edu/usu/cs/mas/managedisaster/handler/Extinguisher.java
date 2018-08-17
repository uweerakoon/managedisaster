package edu.usu.cs.mas.managedisaster.handler;

import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface Extinguisher {

  public void extinguish(FireEntity fire, AgentPlayer agent);
  
  public boolean isFireExtinguised(FireEntity fire);
  
  public void releaseAgent(AgentPlayer agent);
}
