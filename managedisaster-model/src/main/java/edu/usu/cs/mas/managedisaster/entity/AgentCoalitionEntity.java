package edu.usu.cs.mas.managedisaster.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

@AutoProperty
@Entity
@Table (name = "AGENT_COALITION")
public class AgentCoalitionEntity {
  
  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private Long id;

  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "AGENT_ID")
  private AgentEntity agent;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COALITION_ID")
  private CoalitionEntity coalition;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "TIME")
  private Date time;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AgentEntity getAgent() {
    return agent;
  }

  public void setAgent(AgentEntity agent) {
    this.agent = agent;
  }
  
  public AgentCoalitionEntity withAgent(AgentEntity agent) {
    setAgent(agent);
    return this;
  }

  public CoalitionEntity getCoalition() {
    return coalition;
  }

  public void setCoalition(CoalitionEntity coalition) {
    this.coalition = coalition;
  }
  
  public AgentCoalitionEntity withCoalition(CoalitionEntity coalition) {
    setCoalition(coalition);
    return this;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
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
