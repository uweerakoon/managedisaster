package edu.usu.cs.mas.managedisaster.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.Simulator;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.AgentStatEntity;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.BurningBuildingStatEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionBuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionStatEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentStatPersister;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.BurningBuildingStatPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionStatPersister;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import sim.engine.SimState;
import sim.engine.Steppable;

public class DataCuller implements Steppable {

	private static final long serialVersionUID = 605390104733301880L;
	private static final Logger LOGGER = Logger.getLogger(DataCuller.class);
	private static final String DATACULLER_ENABLE = "edu.usu.cs.mas.managedisaster.dataculler.enable";

	@Inject
	private AgentSociety agentSociety;
	@Inject
	private AgentStatPersister agentStatPersister;
	@Inject
	private AgentPersister agentPersister;
	@Inject
	private CoalitionPersister coalitionPersister;
	@Inject
	private CoalitionBuildingPersister coalitionBuildingPersister;
	@Inject
	private CoalitionStatPersister coalitionStatPersister;
	@Inject
	private BuildingPersister buildingPersister;
	@Inject
	private BurningBuildingStatPersister burningBuildingStatPersister;
	@Inject
	private Config config;
	
	@Override
	public void step(SimState state) {
		if(!config.getBoolean(DATACULLER_ENABLE)) {
			return;
		}
		Simulator simulator = (Simulator) state;
		long currentTime = (long)simulator.schedule.getTime();
		LOGGER.info("Saving Agent Statistics: "+currentTime);
		saveAgentStats(currentTime);
		saveCoalitionStat(currentTime);
		saveBurningBuildingStat(currentTime);
	}
	
	private void saveBurningBuildingStat(long currentTime) {
		List<BuildingEntity> buildings = buildingPersister.getAllBuildings();
		for(BuildingEntity building : buildings) {
			if(building.getFires() == null || building.getFires().isEmpty()) {
				continue;
			}
			BurningBuildingStatEntity burningBuildingStatEntity = getBurningBuildingStatEntity(building);
			burningBuildingStatEntity.setTime(currentTime);
			burningBuildingStatPersister.save(burningBuildingStatEntity);
		}
	}
	
	private BurningBuildingStatEntity getBurningBuildingStatEntity(BuildingEntity building) {
		BurningBuildingStatEntity burningBuildingStatEntity = new BurningBuildingStatEntity()
					.withBuilding(building);
		List<FireEntity> fires = building.getFires();
		for(FireEntity fire : fires) {
			burningBuildingStatEntity.withFireAmount(burningBuildingStatEntity.getFireAmount() + fire.getCurrentFireValue())
				.withSmokeAmount(burningBuildingStatEntity.getSmokeAmount() + fire.getCurrentSmokeValue())
				.withWaterAmount(burningBuildingStatEntity.getWaterAmount() + fire.getCurrentWaterValue());
			
		}
		return burningBuildingStatEntity;
	}
	
	private void saveCoalitionStat(long currentTime) {
		List<CoalitionEntity> coalitions = coalitionPersister.getActiveCoalitions();
		for(CoalitionEntity coalition : coalitions) {
			List<CoalitionStatEntity> coalitionStatEntities = getCoalitionStatEntity(coalition);
			for(CoalitionStatEntity coalitionStatEntity : coalitionStatEntities) {
			  coalitionStatEntity.setTime(currentTime);
			  coalitionStatPersister.save(coalitionStatEntity);
			}
		}
	}
	
	private List<CoalitionStatEntity> getCoalitionStatEntity(CoalitionEntity coalition) {
		List<CoalitionBuildingEntity> coalBuilds = coalitionBuildingPersister.getCoalitionBuildings(coalition);
		List<CoalitionStatEntity> coalStats = new ArrayList<>();
		for(CoalitionBuildingEntity coalBuild : coalBuilds) {
		  CoalitionStatEntity coalitionStatEntity = new CoalitionStatEntity()
		      .withCoalition(coalition)
		      .withResourceAmount(coalBuild.getResourceAmount())
		      .withTaskAmount(coalBuild.getTaskAmount())
		      .withBuilding(coalBuild.getBuilding());
		  coalStats.add(coalitionStatEntity);
		}
		return coalStats;  
	}
	
	private void saveAgentStats(long currentTime) {
		Collection<AgentPlayer> agentPlayers = agentSociety.getAllAgentPlayers();
		for(AgentPlayer agentPlayer : agentPlayers) {
			AgentStatEntity agentStatEntity = getAgentStatEntity(agentPlayer);
			agentStatEntity.setTime(currentTime);
			agentStatPersister.save(agentStatEntity);
		}
	}
	
	private AgentStatEntity getAgentStatEntity(AgentPlayer agentPlayer) {
		AgentEntity agentEntity = agentPersister.getAgent(agentPlayer.getId());
		AgentStatEntity agentStatEntity = new AgentStatEntity()
				.withAgent(agentEntity)
				.withStatus(agentPlayer.getStatus());
		return agentStatEntity;
	}

}
