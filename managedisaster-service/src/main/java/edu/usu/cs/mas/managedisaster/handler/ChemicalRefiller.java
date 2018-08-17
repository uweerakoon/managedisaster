package edu.usu.cs.mas.managedisaster.handler;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import sim.engine.SimState;
import sim.engine.Steppable;

public class ChemicalRefiller implements Steppable {
	
	private static final long serialVersionUID = -3918947406027315215L;
	
	private static final double REFILL_THRESHOLD = 0.25;
	
	private MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());
	
	@Inject
	private FireStationPersister fireStationPersister;
	
	@Override
  public void step(SimState state){
		List<FireStationEntity> fireStations = fireStationPersister.getAllActiveFireStations();
		List<FireStationEntity> outofServiceFireStations 
				= fireStations.stream()
						.filter(f -> f.getOutOfService() == true)
						.collect(Collectors.toList());
		if(CollectionUtils.isEmpty(outofServiceFireStations)) {
			return;
		}
		for(FireStationEntity fireStation : outofServiceFireStations) {
			double randomValue = random.nextDouble();
			if(randomValue > REFILL_THRESHOLD) {
				continue;
			}
			fireStation.setChemicalAmount(fireStation.getInitialChemicalAmount());
			fireStation.setOutOfService(false);
		}
	}
}
