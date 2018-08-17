package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

@AutoProperty
@Entity
@Table (name = "ROAD_INTERSECTION")
public class RoadIntersectionEntity {
  
  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Property(policy=PojomaticPolicy.NONE)
  @OneToOne
  @JoinColumn(name = "ROAD_ID")
  private RoadEntity road;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne
  @JoinColumn(name = "SOURCE_INTERSECTION_ID")
  private IntersectionEntity sourceIntersection;
  
  @Property(policy=PojomaticPolicy.NONE)
  @ManyToOne
  @JoinColumn(name = "DESTINATION_INTERSECTION_ID")
  private IntersectionEntity destinationIntersection;
 
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public RoadIntersectionEntity withId(Long id) {
    setId(id);
    return this;
  }
  
  public RoadEntity getRoad() {
    return road;
  }

  public void setRoad(RoadEntity road) {
    this.road = road;
  }

  public RoadIntersectionEntity withRoad(RoadEntity road) {
    setRoad(road);
    return this;
  }
  
  public IntersectionEntity getSourceIntersection() {
    return sourceIntersection;
  }

  public void setSourceIntersection(IntersectionEntity sourceIntersection) {
    this.sourceIntersection = sourceIntersection;
  }

  public RoadIntersectionEntity withSourceIntersection(IntersectionEntity sourceIntersection) {
    setSourceIntersection(sourceIntersection);
    return this;
  }
  
  public IntersectionEntity getDestinationIntersection() {
    return destinationIntersection;
  }

  public void setDestinationIntersection(IntersectionEntity destinationIntersection) {
    this.destinationIntersection = destinationIntersection;
  }

  public RoadIntersectionEntity withDestinationIntersection(IntersectionEntity destinationIntersection) {
    setDestinationIntersection(destinationIntersection);
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
