package edu.usu.cs.mas.managedisaster.collection;

import java.util.HashMap;
import java.util.Map;

import edu.usu.cs.mas.managedisaster.player.FireTruckPlayer;

public class FireTruckCollectionImpl implements FireTruckCollection {

	private Map<Long, FireTruckPlayer> fireTruckCollection = new HashMap<>();
	
	@Override
	public void addFireTruckPlayer(FireTruckPlayer fireTruckPlayer) {
		fireTruckCollection.put(fireTruckPlayer.getId(), fireTruckPlayer);
	}
}
