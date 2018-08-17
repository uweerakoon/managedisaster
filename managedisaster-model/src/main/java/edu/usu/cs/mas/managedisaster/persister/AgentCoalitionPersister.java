package edu.usu.cs.mas.managedisaster.persister;

import edu.usu.cs.mas.managedisaster.entity.AgentCoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;

public interface AgentCoalitionPersister {
  
  public void delete(AgentEntity agent);
  
  public void cleanup();
  
  public void save(AgentCoalitionEntity agentCoalition);
}
