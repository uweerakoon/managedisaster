package edu.usu.cs.mas.managedisaster.handler;

public interface CommunicationHandler {
  /**
   * Add all the agents to the communication link
   */
  public void addAllToCommunicationNetwork();
  
  /**
   * Create communication link between agents according to the 
   * given relation in Communicative table
   */
  public void addCommunicationLinks();
}
