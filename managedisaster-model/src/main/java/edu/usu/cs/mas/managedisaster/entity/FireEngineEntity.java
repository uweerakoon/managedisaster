package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import edu.usu.cs.mas.managedisaster.common.Chemical;

@AutoProperty
@Entity
@Table (name = "FIRE_ENGINE")
public class FireEngineEntity {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "X")
  private Integer x;
  
  @Column(name = "Y")
  private Integer y;
  
  @Column(name = "SPEED")
  private Double speed;
  
  @Column(name = "CHEMICAL")
  @Enumerated(EnumType.STRING)
  private Chemical chemical;
  
  @Column(name = "CHEMICAL_AMOUNT")
  private Double chemicalAmount;
  
  @Column(name = "INITIAL_CHEMICAL_AMOUNT")
  private Double initialChemicalAmount;
  
  @Column(name = "MINIMUM_FIRE_PROXIMITY")
  private Long minimumFireProximity;
  
  @Column(name = "SQUIRT_PRESSURE")
  private Integer squirtPressure;
  
  @Column(name = "FILLING_UP_PRESSURE")
  private Integer fillingUpPressure;
  
  @Column(name = "ACTIVE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean active;
  
  @Column(name = "POWER")
  private Double power;
  
  @Column(name = "INITIAL_POWER")
  private Double initialPower;
  
  @Column(name = "PASSENGER_CAPACITY")
  private Integer capacity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Chemical getChemical() {
		return chemical;
	}

	public void setChemical(Chemical chemical) {
		this.chemical = chemical;
	}

	public Double getChemicalAmount() {
		return chemicalAmount;
	}

	public void setChemicalAmount(Double chemicalAmount) {
		this.chemicalAmount = chemicalAmount;
	}

	public Double getInitialChemicalAmount() {
		return initialChemicalAmount;
	}

	public void setInitialChemicalAmount(Double initialChemicalAmount) {
		this.initialChemicalAmount = initialChemicalAmount;
	}

	public Long getMinimumFireProximity() {
		return minimumFireProximity;
	}

	public void setMinimumFireProximity(Long minimumFireProximity) {
		this.minimumFireProximity = minimumFireProximity;
	}

	public Integer getSquirtPressure() {
		return squirtPressure;
	}

	public void setSquirtPressure(Integer squirtPressure) {
		this.squirtPressure = squirtPressure;
	}

	public Integer getFillingUpPressure() {
		return fillingUpPressure;
	}

	public void setFillingUpPressure(Integer fillingUpPressure) {
		this.fillingUpPressure = fillingUpPressure;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Double getPower() {
		return power;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	public Double getInitialPower() {
		return initialPower;
	}

	public void setInitialPower(Double initialPower) {
		this.initialPower = initialPower;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
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
