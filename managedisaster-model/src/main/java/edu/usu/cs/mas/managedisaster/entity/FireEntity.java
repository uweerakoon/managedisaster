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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import edu.usu.cs.mas.managedisaster.common.Originator;
import edu.usu.cs.mas.managedisaster.message.Severity;

@AutoProperty
@Entity
@Table (name = "FIRE")
public class FireEntity {

  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "X")
  private Integer x;
  
  @Column(name = "Y")
  private Integer y;
  
  @Column(name = "MIN_HEAT")
  private Integer minimumHeat;
  
  @Column(name = "MAX_HEAT")
  private Integer maximumHeat;
  
  @Column(name = "MIN_SMOKE")
  private Integer minimumSmoke;
  
  @Column(name = "MAX_SMOKE")
  private Integer maximumSmoke;
  
  @Column(name = "SMOKE_EVAPORATION_RATE")
  private Double smokeEvaporationRate;
  
  @Column(name = "EVAPORATION_RATE")
  private Double evaporationRate;
  
  @Column(name = "DIFFUSION_RATE")
  private Double diffusionRate;
  
  @Column(name = "FIRE_SPREAD_PROBABILITY")
  private Double fireSpreadProbability;
  
  @Column(name = "STARTING_TIME")
  private Long startingTime;
  
  @Column(name = "MINIMUM_DIFFUSE_PROBABILITY")
  private Double minimumDiffuseProbability;
  
  @Column(name = "MAXIMUM_DIFFUSE_PROBABILITY")
  private Double maximumDiffuseProbability;
  
  @Column(name = "MINIMUM_EVAPORATION_PROBABILITY")
  private Double minimumEvaporationProbability;
  
  @Column(name = "MAXIMUM_EVAPORATION_PROBABILITY")
  private Double maximumEvaporationProbability;
  
  @Column(name = "INCREMENT_PROBABILITY")
  private Double incrementProbability;
  
  @Column(name = "FUSABLE_SMOKE_AMOUNT")
  private Long fusableSmokeAmount;
  
  @Column(name = "FUSABLE_PROBABILITY")
  private Double fusableProbability;
  
  @Column(name = "SMOKE_AMOUNT")
  private Long smokeAmount;
  
  /**
   * Enable or disable during the simulation. Once the fire got exhausted 
   * by the agents, then the system disable the fire
   */
  @Column(name = "ENABLE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean enable;
  
  /**
   * The fire participates in the simulation actively. Is the fire inactive
   * then the fire is not participate in the simulation 
   */
  @Column(name = "ACTIVE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean active;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "ORIGINATOR")
  private Originator originator; 
  
  @Enumerated(EnumType.STRING)
  @Column(name = "SEVERITY")
  private Severity severity;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COALITION_ID")
  private CoalitionEntity coalition;  
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BUILDING_ID", nullable = false)
  private BuildingEntity burningBuilding;
  
  @Transient
  private int fireRadius;
  
  @Transient
  private int smokeRadius;
  
  @Transient
  private boolean isExtinguished;
  
  @Transient
  private double currentFireValue;
  
  @Transient
  private double currentSmokeValue;
  
  @Transient
  private double currentWaterValue;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public FireEntity withId(Long id) {
    setId(id);
    return this;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }
  
  public FireEntity withX(Integer x) {
    setX(x);
    return this;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public FireEntity withY(Integer y) {
    setY(y);
    return this;
  }

  public Integer getMinimumHeat() {
    return minimumHeat;
  }

  public void setMinimumHeat(Integer minimumHeat) {
    this.minimumHeat = minimumHeat;
  }

  public FireEntity withMinimumHeat(Integer minimumHeat) {
    setMinimumHeat(minimumHeat);
    return this;
  }

  public Integer getMaximumHeat() {
    return maximumHeat;
  }

  public void setMaximumHeat(Integer maximumHeat) {
    this.maximumHeat = maximumHeat;
  }

  public FireEntity withMaximumHeat(Integer maximumHeat) {
    setMaximumHeat(maximumHeat);
    return this;
  }

  public Integer getMinimumSmoke() {
    return minimumSmoke;
  }

  public void setMinimumSmoke(Integer minimumSmoke) {
    this.minimumSmoke = minimumSmoke;
  }

  public FireEntity withMinimumSmoke(Integer minimumSmoke) {
    setMinimumSmoke(minimumSmoke);
    return this;
  }
  
  public Integer getMaximumSmoke() {
    return maximumSmoke;
  }

  public void setMaximumSmoke(Integer maximumSmoke) {
    this.maximumSmoke = maximumSmoke;
  }
  
  public FireEntity withMaximumSmoke(Integer maximumSmoke) {
    setMaximumSmoke(maximumSmoke);
    return this;
  }

  public Double getSmokeEvaporationRate() {
    return smokeEvaporationRate;
  }

  public void setSmokeEvaporationRate(Double smokeEvaporationRate) {
    this.smokeEvaporationRate = smokeEvaporationRate;
  }
  
  public FireEntity withSmokeEvaporationRate(Double smokeEvaporationRate) {
    setSmokeEvaporationRate(smokeEvaporationRate);
    return this;
  }

  public Double getEvaporationRate() {
    return evaporationRate;
  }

  public void setEvaporationRate(Double evaporationRate) {
    this.evaporationRate = evaporationRate;
  }

  public FireEntity withEvaporationRate(Double evaporationRate) {
    setEvaporationRate(evaporationRate);
    return this;
  }

  public Double getDiffusionRate() {
    return diffusionRate;
  }

  public void setDiffusionRate(Double diffusionRate) {
    this.diffusionRate = diffusionRate;
  }

  public FireEntity withDiffusionRate(Double diffusionRate) {
    setDiffusionRate(diffusionRate);
    return this;
  }

  public Double getFireSpreadProbability() {
    return fireSpreadProbability;
  }

  public void setFireSpreadProbability(Double fireSpreadProbability) {
    this.fireSpreadProbability = fireSpreadProbability;
  }

  public FireEntity withFireSpreadProbability(Double fireSpreadProbability) {
    setFireSpreadProbability(fireSpreadProbability);
    return this;
  }
  
  public Long getStartingTime() {
    return startingTime;
  }

  public void setStartingTime(Long startingTime) {
    this.startingTime = startingTime;
  }

  public FireEntity withStartingTime(Long startingTime) {
    setStartingTime(startingTime);
    return this;
  }

  public Double getMinimumDiffuseProbability() {
    return minimumDiffuseProbability;
  }

  public void setMinimumDiffuseProbability(Double minimumDiffuseProbability) {
    this.minimumDiffuseProbability = minimumDiffuseProbability;
  }

  public FireEntity withMinimumDiffuseProbability(Double minimumDiffuseProbability) {
    setMinimumDiffuseProbability(minimumDiffuseProbability);
    return this;
  }
  
  public Double getMaximumDiffuseProbability() {
    return maximumDiffuseProbability;
  }

  public void setMaximumDiffuseProbability(Double maximumDiffuseProbability) {
    this.maximumDiffuseProbability = maximumDiffuseProbability;
  }

  public FireEntity withMaximumDiffuseProbability(Double maximumDiffuseProbability) {
    setMaximumDiffuseProbability(maximumDiffuseProbability);
    return this;
  }
  
  public Double getMinimumEvaporationProbability() {
    return minimumEvaporationProbability;
  }

  public void setMinimumEvaporationProbability(Double minimumEvaporationProbability) {
    this.minimumEvaporationProbability = minimumEvaporationProbability;
  }

  public FireEntity withMinimumEvaporationProbability(Double minimumEvaporationProbability) {
    setMinimumEvaporationProbability(minimumEvaporationProbability);
    return this;
  }
  
  public Double getMaximumEvaporationProbability() {
    return maximumEvaporationProbability;
  }

  public void setMaximumEvaporationProbability(Double maximumEvaporationProbability) {
    this.maximumEvaporationProbability = maximumEvaporationProbability;
  }

  public FireEntity withMaximumEvaporationProbability(Double maximumEvaporationProbability) {
    setMaximumEvaporationProbability(maximumEvaporationProbability);
    return this;
  }
  
  public Double getIncrementProbability() {
    return incrementProbability;
  }

  public void setIncrementProbability(Double incrementProbability) {
    this.incrementProbability = incrementProbability;
  }

  public FireEntity withIncrementProbability(Double incrementProbability) {
    setIncrementProbability(incrementProbability);
    return this;
  }
  
  public Long getSmokeAmount() {
    return smokeAmount;
  }

  public void setSmokeAmount(Long smokeAmount) {
    this.smokeAmount = smokeAmount;
  }

  public FireEntity withSmokeAmount(Long smokeAmount) {
    setSmokeAmount(smokeAmount);
    return this;
  }
  
  public Long getFusableSmokeAmount() {
    return fusableSmokeAmount;
  }

  public void setFusableSmokeAmount(Long fusableSmokeAmount) {
    this.fusableSmokeAmount = fusableSmokeAmount;
  }

  public FireEntity withFusableSmokeAmount(Long fusableSmokeAmount) {
    setFusableSmokeAmount(fusableSmokeAmount);
    return this;
  }
  
  public Double getFusableProbability() {
    return fusableProbability;
  }

  public void setFusableProbability(Double fusableProbability) {
    this.fusableProbability = fusableProbability;
  }

  public FireEntity withFusableProbability(Double fusableProbability) {
    setFusableProbability(fusableProbability);
    return this;
  }
  
  public int getFireRadius() {
    return fireRadius;
  }

  public void setFireRadius(int fireRadius) {
    this.fireRadius = fireRadius;
  }

  public FireEntity withFireRadius(int fireRadius) {
    setFireRadius(fireRadius);
    return this;
  }
  
  public int getSmokeRadius() {
    return smokeRadius;
  }

  public void setSmokeRadius(int smokeRadius) {
    this.smokeRadius = smokeRadius;
  }
  
  public FireEntity withSmokeRadius(int smokeRadius) {
    setSmokeRadius(smokeRadius);
    return this;
  }

  public BuildingEntity getBurningBuilding() {
    return burningBuilding;
  }

  public void setBurningBuilding(BuildingEntity burningBuilding) {
    this.burningBuilding = burningBuilding;
  }
  
  public FireEntity withBurningBuilding(BuildingEntity burningBuilding) {
  	setBurningBuilding(burningBuilding);
  	return this;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }
  
  public FireEntity withEnable(boolean enable) {
    setEnable(enable);
    return this;
  }

  public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public FireEntity withActive(boolean active) {
		setActive(active);
		return this;
	}
	
	public Originator getOriginator() {
    return originator;
  }

  public void setOriginator(Originator originator) {
    this.originator = originator;
  }

  public FireEntity withOriginator(Originator originator) {
    setOriginator(originator);
    return this;
  }
  
  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(Severity severity) {
    this.severity = severity;
  }
  
  public FireEntity withSeverity(Severity severity) {
    setSeverity(severity);
    return this;
  }

  public CoalitionEntity getCoalition() {
		return coalition;
	}

	public void setCoalition(CoalitionEntity coalition) {
		this.coalition = coalition;
	}
	
	public FireEntity withCoalition(CoalitionEntity coalition) {
		setCoalition(coalition);
		return this;
	}
  
  public boolean isExtinguished() {
    return isExtinguished;
  }

  public void setExtinguished(boolean isExtinguished) {
    this.isExtinguished = isExtinguished;
  }

  public FireEntity withExtinguished(boolean isExtinguished) {
    setExtinguished(isExtinguished);
    return this;
  }
  
  public double getCurrentFireValue() {
		return currentFireValue;
	}

	public void setCurrentFireValue(double currentFireValue) {
		this.currentFireValue = currentFireValue;
	}

	public double getCurrentSmokeValue() {
		return currentSmokeValue;
	}

	public void setCurrentSmokeValue(double currentSmokeValue) {
		this.currentSmokeValue = currentSmokeValue;
	}

	public double getCurrentWaterValue() {
		return currentWaterValue;
	}

	public void setCurrentWaterValue(double currentWaterValue) {
		this.currentWaterValue = currentWaterValue;
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
