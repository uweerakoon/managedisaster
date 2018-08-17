package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.usu.cs.mas.managedisaster.entity.CommunicativeEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class CommunicativePersisterImpl implements CommunicativePersister{

	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  public CommunicativePersisterImpl() { }
  
  public CommunicativePersisterImpl(HibernateUtil hibernateUtil){
    this.hibernateUtil = hibernateUtil;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<CommunicativeEntity> getAllCommunicatives(){
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select comm from CommunicativeEntity comm";
  	Query query = entityManager.createQuery(strQuery);
    List<CommunicativeEntity> communicativeEntities 
      = query.getResultList();
    return communicativeEntities;
  }
  
}
