package edu.usu.cs.mas.managedisaster.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@Entity
@Table (name = "TEST_TABLE")
public class TestTableEntity {

	public TestTableEntity() { }
	
	public TestTableEntity(String name) {
		this.name = name;
		this.active = false;
		this.date = new Date();
	}
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DATE")
	private Date date;
	
	@Column(name = "ACTIVE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean active;
	
	@Column(name = "SCORE")
  private int score;
	
	@Column(name = "util")
	private Double util;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Double getUtil() {
    return util;
  }

  public void setUtil(Double util) {
    this.util = util;
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
