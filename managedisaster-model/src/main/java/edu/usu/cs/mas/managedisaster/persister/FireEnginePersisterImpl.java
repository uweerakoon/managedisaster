package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.usu.cs.mas.managedisaster.entity.FireEngineEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class FireEnginePersisterImpl implements FireEnginePersister {

	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  @SuppressWarnings("unchecked")
  @Override
	public List<FireEngineEntity> getAllActiveFireEngines() {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select fe from FireEngineEntity fe where fe.active = true";
  	Query query = entityManager.createQuery(strQuery);
  	List<FireEngineEntity> fireEngines = query.getResultList();
  	return fireEngines;
	}
}
