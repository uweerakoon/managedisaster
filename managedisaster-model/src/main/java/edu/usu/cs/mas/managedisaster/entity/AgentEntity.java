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

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;

@AutoProperty
@Entity
@Table (name = "AGENT")
public class AgentEntity {
  
  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "X")
  private Integer x;
  
  @Column(name = "Y")
  private Integer y;
  
  @Column(name = "NAME")
  private String name;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private AgentStatus status;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "INITIAL_STATUS")
  private AgentStatus initialStatus;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "ROLE")
  private AgentRole role;
  
  @Column(name = "COLOR")
  private Integer color;
  
  @Column(name = "SPEED")
  private Double speed;
  
  @Column(name = "VICINITY")
  private Long vicinity;
  
  /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "fromAgent")
  private List<CommunicativeEntity> communications = new ArrayList<CommunicativeEntity>();*/

  @Column(name = "CHEMICAL")
  private String chemical;
  
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
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COALITION_ID")
  private CoalitionEntity coalition;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIRE_STATION_ID")
  private FireStationEntity fireStation;
  
  @Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "agent")
  private List<AgentCoalitionEntity> formingCoalitions = new ArrayList<>();
  
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

  public AgentEntity withX(Integer x){
    setX(x);
    return this;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }
  
  public AgentEntity withY(Integer y){
    setY(y);
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public AgentEntity withName(String name){
    setName(name);
    return this;
  }

  public AgentStatus getStatus() {
    return status;
  }

  public void setStatus(AgentStatus status) {
    this.status = status;
  }
  
  public AgentEntity withStatus(AgentStatus status){
    setStatus(status);
    return this;
  }

  public AgentStatus getInitialStatus() {
		return initialStatus;
	}

	public void setInitialStatus(AgentStatus initialStatus) {
		this.initialStatus = initialStatus;
	}
  
  public AgentEntity withInitialStatus(AgentStatus initialStatus) {
  	setInitialStatus(initialStatus);
    return this;
  }

	public AgentRole getRole() {
    return role;
  }

  public void setRole(AgentRole role) {
    this.role = role;
  }
  
  public AgentEntity withRole(AgentRole role){
    setRole(role);
    return this;
  }

  public Integer getColor() {
    return color;
  }

  public void setColor(Integer color) {
    this.color = color;
  }
  
  public AgentEntity withColor(Integer color){
    setColor(color);
    return this;
  }

  public Double getSpeed() {
    return speed;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }
  
  public AgentEntity withSpeed(Double speed){
    setSpeed(speed);
    return this;
  }
  
  /*public List<CommunicativeEntity> getCommunications() {
    return communications;
  }

  public void setCommunications(List<CommunicativeEntity> communications) {
    this.communications = communications;
  }*/
  
  /*public AgentEntity withCommunications(List<CommunicativeEntity> communications){
    setCommunications(communications);
    return this;
  }*/
  
  public Long getVicinity() {
    return vicinity;
  }

  public void setVicinity(Long vicinity) {
    this.vicinity = vicinity;
  }
  
  public AgentEntity withVicinity(Long vicinity) {
    setVicinity(vicinity);
    return this;
  }

  public String getChemical() {
    return chemical;
  }

  public void setChemical(String chemical) {
    this.chemical = chemical;
  }
  
  public AgentEntity withChemical(String chemical) {
    setChemical(chemical);
    return this;
  }

  public Double getChemicalAmount() {
    return chemicalAmount;
  }

  public void setChemicalAmount(Double chemicalAmount) {
    this.chemicalAmount = chemicalAmount;
  }
  
  public AgentEntity withChemicalAmount(Double chemicalAmount) {
    setChemicalAmount(chemicalAmount);
    return this;
  }

  public Double getInitialChemicalAmount() {
    return initialChemicalAmount;
  }

  public void setInitialChemicalAmount(Double initialChemicalAmount) {
    this.initialChemicalAmount = initialChemicalAmount;
  }

  public AgentEntity withInitialChemicalAmount(Double initialChemicalAmount) {
    setInitialChemicalAmount(initialChemicalAmount);
    return this;
  }
  
  public Long getMinimumFireProximity() {
    return minimumFireProximity;
  }

  public void setMinimumFireProximity(Long minimumFireProximity) {
    this.minimumFireProximity = minimumFireProximity;
  }

  public AgentEntity withMinimumFireProximity(Long minimumFireProximity) {
    setMinimumFireProximity(minimumFireProximity);
    return this;
  }
  
  public Integer getSquirtPressure() {
    return squirtPressure;
  }

  public void setSquirtPressure(Integer squirtPressure) {
    this.squirtPressure = squirtPressure;
  }

  public AgentEntity withSquirtPressure(Integer squirtPressure) {
    setSquirtPressure(squirtPressure);
    return this;
  }
  
  public Integer getFillingUpPressure() {
		return fillingUpPressure;
	}

	public void setFillingUpPressure(Integer fillingUpPressure) {
		this.fillingUpPressure = fillingUpPressure;
	}

	public AgentEntity withFillingUpPressure(Integer fillingUpPressure) {
		setFillingUpPressure(fillingUpPressure);
		return this;
	}
	
	public Boolean getActive() {
		return active;
	}

	public Boolean isActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public AgentEntity withActive(Boolean active) {
    setActive(active);
    return this;
  }
  
  public CoalitionEntity getCoalition() {
		return coalition;
	}

	public void setCoalition(CoalitionEntity coalition) {
		this.coalition = coalition;
	}
	
	public AgentEntity withCoalition(CoalitionEntity coalition) {
		setCoalition(coalition);
		return this;
	}

	
	public FireStationEntity getFireStation() {
		return fireStation;
	}

	public void setFireStation(FireStationEntity fireStation) {
		this.fireStation = fireStation;
	}

	public AgentEntity withFireStation(FireStationEntity fireStation) {
		setFireStation(fireStation);
		return this;
	}
	
	public List<AgentCoalitionEntity> getFormingCoalitions() {
    return formingCoalitions;
  }

  public void setFormingCoalitions(List<AgentCoalitionEntity> formingCoalitions) {
    this.formingCoalitions = formingCoalitions;
  }
  
  public boolean isFormingCoalition(Long coalitionId) {
    boolean hasCoalition = formingCoalitions
                            .stream()
                            .anyMatch(ac -> ac.getCoalition().getId().equals(coalitionId));
    return hasCoalition;
  }
  
  public void addFormingCoalition(AgentCoalitionEntity formingCoalition) {
    formingCoalitions.add(formingCoalition);
  }
  
  public void clearFormingCoalition() {
    formingCoalitions.clear();
  }
  
  public AgentEntity withFormingCoalitions(List<AgentCoalitionEntity> formingCoalitions) {
    setFormingCoalitions(formingCoalitions);
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
