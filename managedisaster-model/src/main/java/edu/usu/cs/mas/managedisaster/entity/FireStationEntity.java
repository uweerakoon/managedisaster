package edu.usu.cs.mas.managedisaster.entity;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

@AutoProperty
@Entity
@Table (name = "FIRE_STATION")
public class FireStationEntity {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
	
	@Column(name = "STATION_X")
  private Integer stationX;
	
	@Column(name = "STATION_Y")
  private Integer stationY;
	
	@Column(name = "ROAD_X")
  private Integer roadX;
	
	@Column(name = "ROAD_Y")
  private Integer roadY;
	
	@Column(name = "CHEMICAL_AMOUNT")
  private Double chemicalAmount;
	
	@Column(name = "INITIAL_CHEMICAL_AMOUNT")
  private Double initialChemicalAmount;
	
	@Column(name = "ACTIVE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean active;
	
	@Column(name = "OUT_OF_SERVICE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean outOfService;
	
	@Property(policy=PojomaticPolicy.NONE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fireStation")
  private List<AgentEntity> agents;
	
	@Property(policy=PojomaticPolicy.NONE)
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "fireStation")
  private List<CoalitionEntity> coalitions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStationX() {
		return stationX;
	}

	public void setStationX(Integer stationX) {
		this.stationX = stationX;
	}

	public Integer getStationY() {
		return stationY;
	}

	public void setStationY(Integer stationY) {
		this.stationY = stationY;
	}

	public Integer getRoadX() {
		return roadX;
	}

	public void setRoadX(Integer roadX) {
		this.roadX = roadX;
	}

	public Integer getRoadY() {
		return roadY;
	}

	public void setRoadY(Integer roadY) {
		this.roadY = roadY;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getOutOfService() {
		return outOfService;
	}

	public void setOutOfService(Boolean outOfService) {
		this.outOfService = outOfService;
	}

	public List<AgentEntity> getAgents() {
		return agents;
	}
	
	public List<AgentEntity> getActiveAgents() {
    return agents
            .stream()
            .filter(ag -> ag.getActive() != null && ag.getActive() == true)
            .collect(Collectors.toList());
  }

	public void setAgents(List<AgentEntity> agents) {
		this.agents = agents;
	}

	public List<CoalitionEntity> getCoalitions() {
    return coalitions;
  }

  public void setCoalitions(List<CoalitionEntity> coalitions) {
    this.coalitions = coalitions;
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
