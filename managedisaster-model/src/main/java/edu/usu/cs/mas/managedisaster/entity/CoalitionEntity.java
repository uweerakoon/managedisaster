package edu.usu.cs.mas.managedisaster.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import edu.usu.cs.mas.managedisaster.common.CoalitionStatus;

@AutoProperty
@Entity
@Table (name = "COALITION")
public class CoalitionEntity {

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private Long id;

  @Column(name = "X")
  private Integer x;

  @Column(name = "Y")
  private Integer y;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private CoalitionStatus status;

  //0 - false and 1 - true
  @Column(name = "FEASIBLE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean feasible;

  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FIRE_STATION_ID")
  private FireStationEntity fireStation;

  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "coalition")
  private List<AgentEntity> agents;

  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "coalition")
  private List<CoalitionForestEntity> coalitionForests;

  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ALLOCATED_FOREST_ID")
  private ForestEntity allocatedForest;
  
  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "coalition")
  private List<AgentCoalitionEntity> formingAgents;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CoalitionEntity withId(Long id) {
    setId(id);
    return this;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public CoalitionEntity withX(Integer x) {
    setX(x);
    return this;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public CoalitionEntity withY(Integer y) {
    setY(y);
    return this;
  }

  public CoalitionStatus getStatus() {
    return status;
  }

  public void setStatus(CoalitionStatus status) {
    this.status = status;
  }

  public CoalitionEntity withStatus(CoalitionStatus status) {
    setStatus(status);
    return this;
  }

  public Boolean getFeasible() {
    return feasible;
  }

  public void setFeasible(Boolean feasible) {
    this.feasible = feasible;
  }

  public CoalitionEntity withFeasible(Boolean feasible) {
    setFeasible(feasible);
    return this;
  }

  public FireStationEntity getFireStation() {
    return fireStation;
  }

  public void setFireStation(FireStationEntity fireStation) {
    this.fireStation = fireStation;
  }

  public CoalitionEntity withFireStation(FireStationEntity fireStation) {
    setFireStation(fireStation);
    return this;
  }

  public List<AgentEntity> getAgents() {
    return agents;
  }

  public void setAgents(List<AgentEntity> agents) {
    this.agents = agents;
  }

  public CoalitionEntity withAgents(List<AgentEntity> agents) {
    setAgents(agents);
    return this;
  }

  public void addAgent(AgentEntity agent){
    if(this.agents == null) {
      this.agents = new ArrayList<>();
    }
    this.agents.add(agent);
  }

  public List<CoalitionForestEntity> getCoalitionForests() {
    return coalitionForests;
  }

  public void setCoalitionForests(List<CoalitionForestEntity> coalitionForests) {
    this.coalitionForests = coalitionForests;
  }

  public CoalitionEntity withCoalitionForests(List<CoalitionForestEntity> coalitionForests) {
    setCoalitionForests(coalitionForests);
    return this;
  }

  public void addCoalitionForest(CoalitionForestEntity coalitionForest) {
    if(this.coalitionForests == null) {
      this.coalitionForests = new ArrayList<>();
    }
    this.coalitionForests.add(coalitionForest);
  }

  public ForestEntity getAllocatedForest() {
    return allocatedForest;
  }

  public void setAllocatedForest(ForestEntity allocatedForest) {
    this.allocatedForest = allocatedForest;
  }

  public CoalitionEntity withAllocatedForest(ForestEntity allocatedForest) {
    setAllocatedForest(allocatedForest);
    return this;
  }
  
  public List<AgentCoalitionEntity> getFormingAgents() {
    return formingAgents;
  }

  public void setFormingAgents(List<AgentCoalitionEntity> formingAgents) {
    this.formingAgents = formingAgents;
  }
  
  public CoalitionEntity withFormingAgents(List<AgentCoalitionEntity> formingAgents) {
    setFormingAgents(formingAgents);
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
