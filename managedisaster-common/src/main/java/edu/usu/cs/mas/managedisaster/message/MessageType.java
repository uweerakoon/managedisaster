package edu.usu.cs.mas.managedisaster.message;

public enum MessageType {
  FIRE, AGENT;
  
  public static MessageType getMessageType(String tweet) {
    for(MessageType messageType : values()) {
      if(tweet.toLowerCase().contains(messageType.toString().toLowerCase())) {
        return messageType;
      }
    }
    return null;
  }
}
