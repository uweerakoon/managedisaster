package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import edu.usu.cs.mas.managedisaster.common.AgentStatus;

@AutoProperty
@Entity
@Table (name = "AGENT_STAT")
public class AgentStatEntity {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
	
	@Property(policy=PojomaticPolicy.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENT_ID")
	private AgentEntity agent;
	
	@Column(name = "TIME_STAMP")
	private long time;
	
	@Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AgentStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public AgentStatEntity withId(Long id) {
		setId(id);
		return this;
	}

	public AgentEntity getAgent() {
		return agent;
	}

	public void setAgent(AgentEntity agent) {
		this.agent = agent;
	}

	public AgentStatEntity withAgent(AgentEntity agent) {
		setAgent(agent);
		return this;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public AgentStatEntity withTime(long time) {
		setTime(time);
		return this;
	}
	
	public AgentStatus getStatus() {
		return status;
	}

	public void setStatus(AgentStatus status) {
		this.status = status;
	}
	
	public AgentStatEntity withStatus(AgentStatus status) {
		setStatus(status);
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
