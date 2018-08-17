package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.FireEngineEntity;

public interface FireEnginePersister {
 
	public List<FireEngineEntity> getAllActiveFireEngines();
	
}
