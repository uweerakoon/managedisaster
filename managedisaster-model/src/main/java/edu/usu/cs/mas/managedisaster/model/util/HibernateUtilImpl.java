package edu.usu.cs.mas.managedisaster.model.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class HibernateUtilImpl implements HibernateUtil{

  private static final Logger LOGGER = Logger.getLogger(HibernateUtilImpl.class);

  private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";
  private static EntityManagerFactory factory;
  private static EntityManager entityManager;
  private static EntityTransaction transaction;

  {
    try {
      factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
      entityManager = factory.createEntityManager();
      transaction = entityManager.getTransaction();
      transaction.begin();
    } catch(Throwable ex) {
      LOGGER.fatal(ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  @Override
  public void shutdown() {
    LOGGER.info("Closing the database Connection");
    transaction.commit();
    entityManager.close();
    factory.close();
  }

  @Override
  public EntityManager getEntityManager() {
    if(entityManager == null){
      LOGGER.fatal("Database connection is not get started");
      System.exit(1);
    }
    return entityManager;
  }

  @Override
  public void commit() {
    transaction.commit();
    transaction = entityManager.getTransaction();
    transaction.begin();
  }

}
