package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentUtilityEntity;
import sim.engine.SimState;

public interface AgentUtilityPersister {
  
  public List<AgentUtilityEntity> getAllAgentUtilities();
  
  public List<AgentUtilityEntity> getAgentUtility(AgentEntity agent);
  
  public void save(AgentUtilityEntity agentUtil);
  
  public void cleanup();
  
  public void setSimState(SimState simState);
}
