package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

//import edu.usu.cs.mas.managedisaster.service.planner.util.CommunicativePK;

@AutoProperty
@Entity
@Table (name = "COMMUNICATIVE")
public class CommunicativeEntity {

  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  /*@ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name = "FROM_AGENT", nullable = false)
  private AgentEntity fromAgent;*/
  @Column(name = "FROM_AGENT", nullable = false)
  private Long fromAgentId;
  
  @Column(name = "TO_AGENT", nullable = false)
  private Long toAgentId;
  
  @Column(name = "WILLINGNESS")
  private Double willingToCommunicate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /*public AgentEntity getFromAgent() {
    return fromAgent;
  }

  public void setFromAgent(AgentEntity fromAgent) {
    this.fromAgent = fromAgent;
  }*/

  public Long getToAgentId() {
    return toAgentId;
  }

  public Long getFromAgentId() {
    return fromAgentId;
  }

  public void setFromAgentId(Long fromAgentId) {
    this.fromAgentId = fromAgentId;
  }

  public void setToAgentId(Long toAgent) {
    this.toAgentId = toAgent;
  }

  public Double getWillingToCommunicate() {
    return willingToCommunicate;
  }

  public void setWillingToCommunicate(Double willingToCommunicate) {
    this.willingToCommunicate = willingToCommunicate;
  }
  
  public CommunicativeEntity withId(Long id){
    setId(id);
    return this;
  }
  
  /*public CommunicativeEntity withFromAgent(AgentEntity fromAgent){
    setFromAgent(fromAgent);
    return this;
  }*/
  
  public CommunicativeEntity withFromAgentId(Long fromAgentId){
    setFromAgentId(fromAgentId);
    return this;
  }
  
  public CommunicativeEntity withToAgentId(Long toAgentId){
    setToAgentId(toAgentId);
    return this;
  }
  
  public CommunicativeEntity withWillingToCommunicate(Double willingToCommunicate) {
    setWillingToCommunicate(willingToCommunicate);
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
