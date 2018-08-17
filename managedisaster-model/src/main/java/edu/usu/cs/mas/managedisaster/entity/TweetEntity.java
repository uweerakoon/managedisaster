package edu.usu.cs.mas.managedisaster.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import edu.usu.cs.mas.managedisaster.message.MessageType;
import edu.usu.cs.mas.managedisaster.message.Severity;

@AutoProperty
@Entity
@Table (name = "TWEET")
public class TweetEntity {

  @Id
  @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
  private Long id;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "SEVERITY")
  private Severity severity;
  
  @Enumerated(EnumType.STRING)
  @Column(name = "MESSAGE_TYPE")
  private MessageType messageType;
  
  @Column(name = "POSTED", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean posted; // 0 - false and 1 - true
  
  @Column(name = "TWEET_TIME")
  private Long tweetTime;
  
  @Column(name = "POSTED_TIME")
  private Long postedTime;
  
  @Column(name = "READ_TIME")
  private Long readTime;
  
  @ManyToOne
  @JoinColumn(name = "BUILDING_ID")
  private BuildingEntity building;
  
  /**
   * Determine whether the record is active to participate in the simulation 
   */
  @Column(name = "ACTIVE", columnDefinition = "TINYINT")
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean active;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TweetEntity withId(Long id) {
    setId(id);
    return this;
  }
  
  public Severity getSeverity() {
    return severity;
  }

  public void setSeverity(Severity severity) {
    this.severity = severity;
  }

  public TweetEntity withSeverity(Severity severity) {
    setSeverity(severity);
    return this;
  }
  
  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }

  public TweetEntity withMessageType(MessageType messageType) {
    setMessageType(messageType);
    return this;
  }
  
  public boolean isPosted() {
    return posted;
  }

  public void setPosted(boolean posted) {
    this.posted = posted;
  }

  public TweetEntity withPosted(boolean posted) {
    setPosted(posted);
    return this;
  }
  
  public Long getTweetTime() {
    return tweetTime;
  }

  public void setTweetTime(Long tweetTime) {
    this.tweetTime = tweetTime;
  }

  public TweetEntity withTweetTime(Long tweetTime) {
    setTweetTime(tweetTime);
    return this;
  }
  
  public BuildingEntity getBuilding() {
    return building;
  }

  public void setBuilding(BuildingEntity building) {
    this.building = building;
  }

  public TweetEntity withBuilding(BuildingEntity building) {
    setBuilding(building);
    return this;
  }
  
  public Long getPostedTime() {
    return postedTime;
  }

  public void setPostedTime(Long postedTime) {
    this.postedTime = postedTime;
  }

  public TweetEntity withPostedTime(Long postedTime) {
    setPostedTime(postedTime);
    return this;
  }
  
  public Long getReadTime() {
    return readTime;
  }

  public void setReadTime(Long readTime) {
    this.readTime = readTime;
  }
  
  public TweetEntity withReadTime(Long readTime) {
    setReadTime(readTime);
    return this;
  }

  public Boolean getActive() {
  	return active;
  }
  
  public void setActive(Boolean active) {
		this.active = active;
	}
  
  public TweetEntity withActive(Boolean active) {
  	setActive(active);
  	return this;
  }

	@Override
  public String toString() {
    return Pojomatic.toString(this);
  }
  
  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }
  
  @Override
  public boolean equals(Object other) {
    return Pojomatic.equals(this, other);
  }
}
