package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import sim.util.MutableInt2D;

@AutoProperty
@Entity
@Table (name = "INTERSECTION")
public class IntersectionEntity implements Comparable<IntersectionEntity>{

  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Column(name = "X")
  private Integer x;
  
  @Column(name = "Y")
  private Integer y;
  
  @Transient
  private int distance;
  
  @Transient
  private IntersectionEntity previousIntersection;
  
  @Transient
  private MutableInt2D location;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public IntersectionEntity withId(Long id) {
    setId(id);
    return this;
  }
  
  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public IntersectionEntity withX(Integer x) {
    setX(x);
    return this;
  }
  
  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public IntersectionEntity withY(Integer y) {
    setY(y);
    return this;
  }
  
  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public IntersectionEntity withDistance(int distance) {
    setDistance(distance);
    return this;
  }
  
  public IntersectionEntity getPreviousIntersection() {
    return previousIntersection;
  }

  public void setPreviousIntersection(IntersectionEntity previousIntersection) {
    this.previousIntersection = previousIntersection;
  }
  
  public IntersectionEntity withPreviousIntersection(IntersectionEntity previousIntersection) {
    setPreviousIntersection(previousIntersection);
    return this;
  }
  
  public MutableInt2D getLocation() {
  	if(this.location == null) {
  		this.location = new MutableInt2D(x, y);
  	}
  	return this.location;
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
  
  @Override
  public int compareTo(IntersectionEntity other){
    return Integer.compare(distance, other.getDistance());
  }
}
