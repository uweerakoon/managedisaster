package edu.usu.cs.mas.managedisaster.model;

import org.pojomatic.Pojomatic;

import edu.usu.cs.mas.managedisaster.common.Chemical;

public class FireTruckModel {

	private Long id;
	private Integer x;
	private Integer y;
	private Double speed;
	private Chemical chemical;
	private Double chemicalAmount;
	private Double initialChemicalAmount;
	private Long minimumFireProximity;
	private Integer squirtPressure;
	private Integer fillingUpPressure;
	private Boolean active;
	private Double power;
	private Double initialPower;
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
