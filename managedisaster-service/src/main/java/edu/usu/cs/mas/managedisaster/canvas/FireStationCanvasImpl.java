package edu.usu.cs.mas.managedisaster.canvas;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

public class FireStationCanvasImpl implements FireStationCanvas {

	private static final Logger LOGGER = Logger.getLogger(FireStationCanvasImpl.class);
	
	private final String name = "Fire Stations";
	
	private FastValueLabeledGridPortrayal2D fireStationPortrayal;
	private IntGrid2D fireStationGrid;
	private List<FireStationEntity> fireStations;
	
	@Inject
  private FireStationPersister fireStationPersister;
	
	public FireStationCanvasImpl() { }
	
	public FireStationCanvasImpl(FireStationPersister fireStationPersister) {
		this.fireStationPersister = fireStationPersister;
	}
	
	@Override
	public void drawFireStations() {
		if(fireStations == null) {
			fireStations = fireStationPersister.getAllActiveFireStations();
		}
		for(FireStationEntity fireStation : fireStations) {
			int startX = fireStation.getStationX();
			int startY = fireStation.getStationY();
			for(int i = startX; i < startX + 3; i++) {
				for(int j = startY; j < startY + 3; j++) {
					fireStationGrid.field[i][j] = fireStation.getId().intValue();
				}
			}
		}
	}

	@Override
	public FastValueLabeledGridPortrayal2D getFireStationPortrayal() {
		return fireStationPortrayal;
	}

	@Override
	public void setFireStationPortrayal(FastValueLabeledGridPortrayal2D fireStationPortrayal) {
		this.fireStationPortrayal = fireStationPortrayal;
		for(FireStationEntity fireStation : fireStations) {
		  if(!fireStation.getOutOfService()) {
        fireStationPortrayal.addLabel(new Int2D(fireStation.getStationX(), fireStation.getStationY()), fireStation.getId().toString());
      }
		}
	}
	
	@Override
	public void addLabel(Int2D position, String label) {
		fireStationPortrayal.addLabel(position, label);
	}

	@Override
	public IntGrid2D getFireStationGrid() {
		return fireStationGrid;
	}

	@Override
	public void setFireStationGrid(IntGrid2D fireStationGrid) {
		this.fireStationGrid = fireStationGrid;
	}

	@Override
	public String getName() {
		return name;
	}
}
