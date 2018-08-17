package edu.usu.cs.mas.managedisaster.collection;

import java.util.Collection;
import java.util.List;

import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface AgentSociety {

  /**
   * Add an agent player to the society
   * @param agentPlayer
   */
  public void addAgentPlayer(AgentPlayer agentPlayer);
  /**
   * Get all the agents in the society
   * @return a list of all agents
   */
  public Collection<AgentPlayer> getAllAgentPlayers();
  /**
   * Return the corresponding agent for the id
   * @param id
   * @return agent player
   */
  public AgentPlayer getAgent(Long id);
  
  public List<AgentPlayer> getAgents(List<Long> ids);
  
  /**
   * Return all the agents in the status of coalition formation
   * @return list of CF agents
   */
  public List<AgentPlayer> getCoalitionFormationAgents();
}
