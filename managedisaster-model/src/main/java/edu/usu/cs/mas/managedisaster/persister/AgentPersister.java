package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;

public interface AgentPersister {
  /**
   * Fetch all the agents
   * @return list of agents
   */
  public List<AgentEntity> getAllActiveAgents();
  
  public AgentEntity getAgent(Long id);
  
  public void cleanup();
  
  public void save(AgentEntity agent);
}
