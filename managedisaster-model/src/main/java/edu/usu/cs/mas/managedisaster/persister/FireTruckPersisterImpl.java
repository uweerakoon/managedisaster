package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.usu.cs.mas.managedisaster.entity.FireTruckEntity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;

public class FireTruckPersisterImpl implements FireTruckPersister {

	private EntityManager entityManager;
  
  @Inject
  private HibernateUtil hibernateUtil;
  
  @SuppressWarnings("unchecked")
  @Override
	public List<FireTruckEntity> getAllActiveFireTrucks() {
  	entityManager = hibernateUtil.getEntityManager();
  	String strQuery = "select ft from FireTruckEntity ft where ft.active = true";
  	Query query = entityManager.createQuery(strQuery);
		List<FireTruckEntity> fireTrucks = query.getResultList();
		return fireTrucks;
	}
}
