package edu.usu.cs.mas.managedisaster.message;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.TweetEntity;

@AutoProperty
public class Message {

  private MessageType messageType;
  private Severity severity;
  private ForestEntity forest;
  private Long tweetId;
  
  public Message() { }
  
  public Message(TweetEntity tweet) {
    this.messageType = tweet.getMessageType();
    this.severity = tweet.getSeverity();
    this.forest = tweet.getForest();
  }
  
  public MessageType getMessageType() {
    return messageType;
  }
  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }
  public Message withMessageType(MessageType messageType) {
    setMessageType(messageType);
    return this;
  }
  public Severity getSeverity() {
    return severity;
  }
  public void setSeverity(Severity severity) {
    this.severity = severity;
  }
  public Message withSeverity(Severity severity) {
    setSeverity(severity);
    return this;
  }
  public ForestEntity getForest() {
    return forest;
  }
  public void setForest(ForestEntity forest) {
    this.forest = forest;
  }
  public Message withForest(ForestEntity forest) {
    setForest(forest);
    return this;
  }
  
  public Long getTweetId() {
    return tweetId;
  }

  public void setTweetId(Long tweetId) {
    this.tweetId = tweetId;
  }

  public Message withTweetId(Long tweetId) {
    setTweetId(tweetId);
    return this;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
  
  @Override
  public boolean equals(Object other) {
    return Pojomatic.equals(this, other);
  }
  
  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }
  
}
