package edu.usu.cs.mas.managedisaster.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class AgentSocietyImpl implements AgentSociety{
  
  private Map<Long, AgentPlayer> agentSoceity = new HashMap<>();

  @Override
  public void addAgentPlayer(AgentPlayer agentPlayer) {
    agentSoceity.put(agentPlayer.getId(), agentPlayer);
  }
  
  @Override
  public Collection<AgentPlayer> getAllAgentPlayers() {
    return agentSoceity.values();
  }
  
  @Override
  public AgentPlayer getAgent(Long id){
    return agentSoceity.get(id);
  }
  
  @Override
  public List<AgentPlayer> getAgents(List<Long> ids) {
    List<AgentPlayer> agents = new ArrayList<>();
    for(Long id : ids) {
      agents.add(agentSoceity.get(id));
    }
    return agents;
  }
  
  @Override
  public List<AgentPlayer> getCoalitionFormationAgents() {
  	List<AgentPlayer> coalitionFormationAgents 
  			= agentSoceity.values()
  					.stream()
  					.filter(x -> AgentStatus.FORMING_COALITIONS == x.getStatus())
  					.collect(Collectors.toList());
  	return coalitionFormationAgents;
  }
}
