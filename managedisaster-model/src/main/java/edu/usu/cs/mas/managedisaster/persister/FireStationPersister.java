package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;

public interface FireStationPersister {

	public FireStationEntity getFireStation(Long id);
	
	public List<FireStationEntity> getAllActiveFireStations();
	
	public FireStationEntity getClosestFireStation(double x, double y, Long vicinity);
	
	public FireStationEntity save(FireStationEntity fireStation);
	
	public void cleanup();
}
