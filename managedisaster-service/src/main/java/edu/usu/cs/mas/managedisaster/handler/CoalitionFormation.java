package edu.usu.cs.mas.managedisaster.handler;

import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import sim.engine.Steppable;

public interface CoalitionFormation extends Steppable {

  public void release(AgentPlayer agentPlayer);
}
