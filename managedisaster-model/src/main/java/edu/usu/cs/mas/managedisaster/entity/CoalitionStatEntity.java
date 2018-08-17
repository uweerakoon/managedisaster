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
@Table (name = "COALITION_STAT")
public class CoalitionStatEntity {

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
  @JoinColumn(name = "BUILDING_ID")
  private BuildingEntity building;
	
	@Column(name = "TIME_STAMP")
	private long time;
	
	@Column(name = "RESOURCE_AMOUNT")
	private double resourceAmount;
	
	@Column(name = "TASK_AMOUNT")
	private double taskAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CoalitionEntity getCoalition() {
		return coalition;
	}

	public void setCoalition(CoalitionEntity coalition) {
		this.coalition = coalition;
	}
	
	public CoalitionStatEntity withCoalition(CoalitionEntity coalition) {
		setCoalition(coalition);
		return this;
	}

	public BuildingEntity getBuilding() {
    return building;
  }

  public void setBuilding(BuildingEntity building) {
    this.building = building;
  }

  public CoalitionStatEntity withBuilding(BuildingEntity building) {
    setBuilding(building);
    return this;
  }
  
  public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public CoalitionStatEntity withTime(long time) {
		setTime(time);
		return this;
	}

	public double getResourceAmount() {
		return resourceAmount;
	}

	public void setResourceAmount(double resourceAmount) {
		this.resourceAmount = resourceAmount;
	}

	public CoalitionStatEntity withResourceAmount(double resourceAmount) {
		setResourceAmount(resourceAmount);
		return this;
	}

	public double getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(double taskAmount) {
		this.taskAmount = taskAmount;
	}

	public CoalitionStatEntity withTaskAmount(double taskAmount) {
		setTaskAmount(taskAmount);
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
