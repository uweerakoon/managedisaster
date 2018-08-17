package edu.usu.cs.mas.managedisaster.model;

import java.awt.Color;
import java.util.List;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import sim.util.MutableInt2D;

@AutoProperty
public class AgentModel{
  
  private Long id;
  
  private Integer x;
  
  private Integer y;
  
  private String name;
  
  private AgentStatus status;
  
  private AgentStatus initialStatus;
  
  private AgentRole role;
  
  private Color color;
  
  private double speed;
  
  private Long vicinity;
  
  private Chemical chemical;
  
  private Double chemicalAmount;
  
  private Double initialChemicalAmount;
  
  private Long minimumFireProximity;
  
  private Integer squirtPressure;
  
  private Integer fillingUpPressure;
  
  private MutableInt2D waterImpactCenter;
  
  private int waterImpactRadius;
  
  @Property(policy=PojomaticPolicy.NONE)
  private FireEntity fire;
  
  @Property(policy=PojomaticPolicy.NONE)
  private List<FireEntity> closeFires;
  
  @Property(policy=PojomaticPolicy.NONE)
  private CoalitionEntity coalition;
  
  @Property(policy=PojomaticPolicy.NONE)
  private FireStationEntity fireStation;
  
  public AgentModel(){ }
  
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AgentStatus getStatus() {
    return status;
  }

  public void setStatus(AgentStatus status) {
    this.status = status;
  }

  public AgentStatus getInitialStatus() {
		return initialStatus;
	}

	public void setInitialStatus(AgentStatus initialStatus) {
		this.initialStatus = initialStatus;
	}
	
  public AgentRole getRole() {
    return role;
  }

  public void setRole(AgentRole role) {
    this.role = role;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }
  
  public AgentModel withId(Long id){
    this.setId(id);
    return this;
  }
  
  public AgentModel withX(Integer x){
    this.setX(x);
    return this;
  }
  
  public AgentModel withY(Integer y){
    this.setY(y);
    return this;
  }
  
  public AgentModel withName(String name){
    this.setName(name);;
    return this;
  }
  
  public AgentModel withStatus(AgentStatus status){
    this.setStatus(status);
    return this;
  }
  
  public AgentModel withRole(AgentRole role){
    this.setRole(role);
    return this;
  }
  
  public AgentModel with(Color color){
    this.setColor(color);
    return this;
  }
  
  public AgentModel withSpeed(Double speed){
    this.setSpeed(speed);
    return this;
  }
  
  public Long getVicinity() {
    return vicinity;
  }

  public void setVicinity(Long vicinity) {
    this.vicinity = vicinity;
  }
  
  public AgentModel withVicinity(Long vicinity) {
    setVicinity(vicinity);
    return this;
  }

  public Chemical getChemical() {
    return chemical;
  }

  public void setChemical(Chemical chemical) {
    this.chemical = chemical;
  }

  public AgentModel withChemical(Chemical chemical) {
    setChemical(chemical);
    return this;
  }
  
  public Double getChemicalAmount() {
    return chemicalAmount;
  }

  public void setChemicalAmount(Double chemicalAmount) {
    this.chemicalAmount = chemicalAmount;
  }

  public AgentModel withChemicalAmount(Double chemicalAmount) {
    setChemicalAmount(chemicalAmount);
    return this;
  }
  
  public Double getInitialChemicalAmount() {
		return initialChemicalAmount;
	}

	public void setInitialChemicalAmount(Double initialChemicalAmount) {
		this.initialChemicalAmount = initialChemicalAmount;
	}

	public AgentModel withInitialChemicalAmount(Double initialChemicalAmount) {
		setInitialChemicalAmount(initialChemicalAmount);
		return this;
	}
	
	public Long getMinimumFireProximity() {
    return minimumFireProximity;
  }

  public void setMinimumFireProximity(Long minimumFireProximity) {
    this.minimumFireProximity = minimumFireProximity;
  }
  
  public AgentModel withMinimumFireProximity(Long minimumFireProximity) {
    setMinimumFireProximity(minimumFireProximity);
    return this;
  }
  
  public Integer getSquirtPressure() {
    return squirtPressure;
  }

  public void setSquirtPressure(Integer squirtPressure) {
    this.squirtPressure = squirtPressure;
  }

  public AgentModel withSquirtPressure(Integer squirtPressure) {
    setSquirtPressure(squirtPressure);
    return this;
  }

  public Integer getFillingUpPressure() {
		return fillingUpPressure;
	}

	public void setFillingUpPressure(Integer fillingUpPressure) {
		this.fillingUpPressure = fillingUpPressure;
	}

	public AgentModel withFillingUpPressure(Integer fillingUpPressure) {
		setFillingUpPressure(fillingUpPressure);
		return this;
	}
	
  public MutableInt2D getWaterImpactCenter() {
    return waterImpactCenter;
  }

  public void setWaterImpactCenter(MutableInt2D waterImpactCenter) {
    this.waterImpactCenter = waterImpactCenter;
  }

  public int getWaterImpactRadius() {
    return waterImpactRadius;
  }

  public void setWaterImpactRadius(int waterImpactRadius) {
    this.waterImpactRadius = waterImpactRadius;
  }

  public FireEntity getFire() {
    return fire;
  }

  public void setFire(FireEntity fire) {
    this.fire = fire;
  }

  public AgentModel withFire(FireEntity fire) {
    setFire(fire);
    return this;
  }
  
  public List<FireEntity> getCloseFires() {
    return closeFires;
  }

  public void setCloseFires(List<FireEntity> closeFires) {
    this.closeFires = closeFires;
  }
  
  public AgentModel withCloseFires(List<FireEntity> closeFires) {
    setCloseFires(closeFires);
    return this;
  }

  public CoalitionEntity getCoalition() {
		return coalition;
	}

	public void setCoalition(CoalitionEntity coalition) {
		this.coalition = coalition;
	}
	
	public AgentModel withCoalition(CoalitionEntity coalition) {
		setCoalition(coalition);
		return this;
	}

	public FireStationEntity getFireStation() {
		return fireStation;
	}

	public void setFireStation(FireStationEntity fireStation) {
		this.fireStation = fireStation;
	}

	public AgentModel withFireStation(FireStationEntity fireStation) {
		setFireStation(fireStation);
		return this;
	}
	
	@Override
  public boolean equals(Object other){
    return Pojomatic.equals(this, other);
  }
  
  @Override
  public String toString(){
    return Pojomatic.toString(this);
  }
  
  @Override
  public int hashCode(){
    return Pojomatic.hashCode(this);
  }
}
