package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentStatEntity;

public interface AgentStatPersister {
  
  public List<AgentStatEntity> getAllAgentStats();
  
  public List<AgentStatEntity> getAgentStat(AgentEntity agent);
  
  public void save(AgentStatEntity agentStat);
  
  public void cleanup();
}
