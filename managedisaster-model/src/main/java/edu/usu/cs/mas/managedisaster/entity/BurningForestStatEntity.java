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
@Table (name = "BURNING_FOREST_STAT")
public class BurningForestStatEntity {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
	
	@Property(policy=PojomaticPolicy.NONE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOREST_ID")
	private ForestEntity forest;
	
	@Column(name = "TIME_STAMP")
	private long time;
	
	@Column(name = "FIRE_AMOUNT")
	private double fireAmount;
	
	@Column(name = "SMOKE_AMOUNT")
	private double smokeAmount;
	
	@Column(name = "WATER_AMOUNT")
	private double waterAmount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BurningForestStatEntity withId(Long id) {
		setId(id);
		return this;
	}

	public ForestEntity getForest() {
		return forest;
	}

	public void setForest(ForestEntity forest) {
		this.forest = forest;
	}

	public BurningForestStatEntity withForest(ForestEntity forest) {
		setForest(forest);
		return this;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public BurningForestStatEntity withTime(long time) {
		setTime(time);
		return this;
	}

	public double getFireAmount() {
		return fireAmount;
	}

	public void setFireAmount(double fireAmount) {
		this.fireAmount = fireAmount;
	}

	public BurningForestStatEntity withFireAmount(double fireAmount) {
		setFireAmount(fireAmount);
		return this;
	}

	public double getSmokeAmount() {
		return smokeAmount;
	}

	public void setSmokeAmount(double smokeAmount) {
		this.smokeAmount = smokeAmount;
	}

	public BurningForestStatEntity withSmokeAmount(double smokeAmount) {
		setSmokeAmount(smokeAmount);
		return this;
	}

	public double getWaterAmount() {
		return waterAmount;
	}

	public void setWaterAmount(double waterAmount) {
		this.waterAmount = waterAmount;
	}

	public BurningForestStatEntity withWaterAmount(double waterAmount) {
		setWaterAmount(waterAmount);
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
