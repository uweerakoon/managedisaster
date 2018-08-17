package edu.usu.cs.mas.managedisaster.handler;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.entity.CommunicativeEntity;
import edu.usu.cs.mas.managedisaster.network.CommunicationNetwork;
import edu.usu.cs.mas.managedisaster.persister.CommunicativePersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class CommunicationHandlerImpl implements CommunicationHandler{
  
  @Inject
  private AgentSociety agentSociety;
  @Inject
  private CommunicationNetwork communicationNetwork;
  @Inject
  private CommunicativePersister communicativePersister;
  
  public CommunicationHandlerImpl() { }
  
  public CommunicationHandlerImpl(AgentSociety agentSociety,
      CommunicationNetwork communicationNetwork,
      CommunicativePersister communicativePersister){
    this.agentSociety = agentSociety;
    this.communicationNetwork = communicationNetwork;
    this.communicativePersister = communicativePersister;
  }
  
  @Override
  public void addAllToCommunicationNetwork(){
    Collection<AgentPlayer> agentPlayers = agentSociety.getAllAgentPlayers();
    for(AgentPlayer agentPlayer : agentPlayers){
      communicationNetwork.addAgent(agentPlayer);
    }
  }
  
  @Override
  public void addCommunicationLinks(){
    List<CommunicativeEntity> entities = communicativePersister.getAllCommunicatives();
    for(CommunicativeEntity entity : entities){
      AgentPlayer fromAgent = agentSociety.getAgent(entity.getFromAgentId());
      AgentPlayer toAgent = agentSociety.getAgent(entity.getToAgentId());
      communicationNetwork.addLink(fromAgent, toAgent, entity.getWillingToCommunicate());
    }
  }
}
