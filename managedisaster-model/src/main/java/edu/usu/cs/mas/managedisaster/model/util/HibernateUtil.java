package edu.usu.cs.mas.managedisaster.model.util;

import javax.persistence.EntityManager;

public interface HibernateUtil {
  
  /**
   * Return the session factory for the database
   * @return session factory
   */
	public EntityManager getEntityManager();
  
  /**
   * Shut down the connection
   */
  public void shutdown();
  
  public void commit();
  
}
