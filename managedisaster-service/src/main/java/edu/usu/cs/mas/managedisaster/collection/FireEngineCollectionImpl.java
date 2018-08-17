package edu.usu.cs.mas.managedisaster.collection;

import java.util.HashMap;
import java.util.Map;

import edu.usu.cs.mas.managedisaster.player.FireEnginePlayer;

public class FireEngineCollectionImpl implements FireEngineCollection {

	private Map<Long, FireEnginePlayer> fireEngineCollection = new HashMap<>();
	
	@Override
	public void addFireEnginePlayer(FireEnginePlayer fireEnginePlayer) {
		fireEngineCollection.put(fireEnginePlayer.getId(), fireEnginePlayer);
	}
}
