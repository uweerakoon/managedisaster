package edu.usu.cs.mas.managedisaster.network;

import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import sim.field.network.Network;

public class CommunicationNetworkImpl implements CommunicationNetwork{
  
  private static final boolean DIRECTED_EDGES = false;
  
  private Network communicationNetwork = new Network(DIRECTED_EDGES);
  
  @Override
  public void addAgent(AgentPlayer agentPlayer){
    communicationNetwork.addNode(agentPlayer);
  }
  
  @Override
  public void addLink(AgentPlayer fromAgentPlayer, AgentPlayer toAgentPlayer, Double communicative){
    communicationNetwork.addEdge(fromAgentPlayer, toAgentPlayer, communicative);
  }
}
