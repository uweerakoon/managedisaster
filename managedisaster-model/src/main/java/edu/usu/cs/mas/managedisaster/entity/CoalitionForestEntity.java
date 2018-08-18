package edu.usu.cs.mas.managedisaster.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

import edu.usu.cs.mas.managedisaster.common.CoalitionForestStatus;
import edu.usu.cs.mas.managedisaster.common.UtilityAlgorithm;

@AutoProperty
@Entity
@Table (name = "COALITION_FOREST")
public class CoalitionForestEntity {

  @Id
  @GeneratedValue(generator="increment")
  @GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "COALITION_ID")
  private CoalitionEntity coalition;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FOREST_ID")
  private ForestEntity forest;
  
  @Column(name = "RESOURCE_AMOUNT")
  private Double resourceAmount;
  
  @Column(name = "TASK_AMOUNT")
  private Double taskAmount;
  
  @Column(name = "UTILITY")
  private Double utility;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "ALGORITHM")
  private UtilityAlgorithm algorithm;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private CoalitionForestStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public CoalitionForestEntity withId(Long id) {
    setId(id);
    return this;
  }

  public CoalitionEntity getCoalition() {
    return coalition;
  }

  public void setCoalition(CoalitionEntity coalition) {
    this.coalition = coalition;
  }
  
  public CoalitionForestEntity withCoalition(CoalitionEntity coalition) {
    setCoalition(coalition);
    return this;
  }

  public ForestEntity getForest() {
    return forest;
  }

  public void setForest(ForestEntity forest) {
    this.forest = forest;
  }
  
  public CoalitionForestEntity withForest(ForestEntity forest) {
    setForest(forest);
    return this;
  }

  public Double getResourceAmount() {
    return resourceAmount;
  }

  public void setResourceAmount(Double resourceAmount) {
    // Need to set up the precision correctly (10,2)
    BigDecimal bigDecimal = new BigDecimal(resourceAmount);
    bigDecimal.setScale(2, RoundingMode.HALF_UP);
    this.resourceAmount = bigDecimal.doubleValue();
  }
  
  public CoalitionForestEntity withResourceAmount(Double resourceAmount) {
    setResourceAmount(resourceAmount);
    return this;
  }

  public Double getTaskAmount() {
    return taskAmount;
  }

  public void setTaskAmount(Double taskAmount) {
    // Need to set up the precision correctly (10,2)
    BigDecimal bigDecimal = new BigDecimal(taskAmount);
    bigDecimal.setScale(2, RoundingMode.HALF_UP);
    this.taskAmount = bigDecimal.doubleValue();
  }
  
  public CoalitionForestEntity withTaskAmount(Double taskAmount) {
    withTaskAmount(taskAmount);
    return this;
  }

  public Double getUtility() {
    return utility;
  }

  public void setUtility(Double utility) {
    // Need to set up the precision correctly (10,2)
    BigDecimal bigDecimal = new BigDecimal(utility);
    bigDecimal.setScale(2, RoundingMode.HALF_UP);
    this.utility = bigDecimal.doubleValue();
  }
  
  public CoalitionForestEntity withUtility(Double utility) {
    setUtility(utility);
    return this;
  }

  public UtilityAlgorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(UtilityAlgorithm algorithm) {
    this.algorithm = algorithm;
  }
  
  public CoalitionForestEntity withAlgorithm(UtilityAlgorithm algorithm) {
    setAlgorithm(algorithm);
    return this;
  }

  public CoalitionForestStatus getStatus() {
    return status;
  }

  public void setStatus(CoalitionForestStatus status) {
    this.status = status;
  }

  public CoalitionForestEntity withStatus(CoalitionForestStatus status) {
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
