package edu.usu.cs.mas.managedisaster.temporary;

import java.util.Date;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public class Tweet {
  long id;
  String text;
  Date timeStamp;
  public Tweet(long id, String text, Date timeStamp) {
    this.id = id;
    this.text = text;
    this.timeStamp = timeStamp;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }
  public Date getTimeStamp() {
    return timeStamp;
  }
  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
