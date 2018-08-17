package edu.usu.cs.mas.managedisaster.service.planner.util;

import java.io.Serializable;

import org.pojomatic.Pojomatic;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;

public class CommunicativePK implements Serializable{

  private static final long serialVersionUID = 2556237337339281557L;
  
  private AgentEntity fromAgent;
  
  private AgentEntity toAgent;
  
  public CommunicativePK(){ }
  
  public CommunicativePK(AgentEntity fromAgent, AgentEntity toAgent){
    this.fromAgent = fromAgent;
    this.toAgent = toAgent;
  }
  
  @Override
  public boolean equals(Object o){
    return Pojomatic.equals(this, o);
  }
  
  @Override
  public int hashCode(){
    return Pojomatic.hashCode(this);
  }
  
  @Override
  public String toString(){
    return Pojomatic.toString(this);
  }

}
