package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

@AutoProperty
@Entity
@Table (name = "AGENT_UTILITY")
public class AgentUtilityEntity {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "TIME")
  private long time;
	
	@Property(policy=PojomaticPolicy.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENT_ID")
	private AgentEntity agent;
	
	@Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COALITION_ID")
  private CoalitionEntity coalition;
	
  @Column(name = "UTILITY")
  private Double utility;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public AgentUtilityEntity withId(Long id) {
		setId(id);
		return this;
	}
  
  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public AgentUtilityEntity withTime(long time) {
    setTime(time);
    return this;
  }

	public AgentEntity getAgent() {
		return agent;
	}

	public void setAgent(AgentEntity agent) {
		this.agent = agent;
	}

	public AgentUtilityEntity withAgent(AgentEntity agent) {
		setAgent(agent);
		return this;
	}
	
	public CoalitionEntity getCoalition() {
    return coalition;
  }

  public void setCoalition(CoalitionEntity coalition) {
    this.coalition = coalition;
  }
  
  public AgentUtilityEntity withCoalition(CoalitionEntity coalition) {
    setCoalition(coalition);
    return this;
  }

  public Double getUtility() {
    return utility;
  }

  public void setUtility(Double utility) {
    this.utility = utility;
  }
  
  public AgentUtilityEntity withUtility(Double utility) {
    setUtility(utility);
    return this;
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
