package edu.usu.cs.mas.managedisaster.network;

import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public interface CommunicationNetwork {

  /**
   * Adding an agent to the the communication graph
   * @param agentPlayer
   */
  public void addAgent(AgentPlayer agentPlayer);
  
  /**
   * Add a communication link between two agents
   * @param fromAgentPlayer
   * @param toAgentPlayer
   * @param communicative
   */
  public void addLink(AgentPlayer fromAgentPlayer, AgentPlayer toAgentPlayer, Double communicative);
}
