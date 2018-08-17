package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.CommunicativeEntity;

public interface CommunicativePersister {
  /**
   * Get all the communicative links from the database
   * @return all communicative entities
   */
  public List<CommunicativeEntity> getAllCommunicatives();
}
