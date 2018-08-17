package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.FireTruckEntity;

public interface FireTruckPersister {

	public List<FireTruckEntity> getAllActiveFireTrucks();
	
}
