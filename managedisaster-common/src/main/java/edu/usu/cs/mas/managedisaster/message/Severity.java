package edu.usu.cs.mas.managedisaster.message;

public enum Severity {
  SEVERE, NORMAL, MILD; 
  
  public static Severity getSeverity(String tweet) {
    for(Severity severity : values()) {
      if(tweet.toLowerCase().contains(severity.toString().toLowerCase())) {
        return severity;
      }
    }
    return null;
  }
}
