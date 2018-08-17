package edu.usu.cs.mas.managedisaster.canvas;

import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

public interface FireStationCanvas {

	public void drawFireStations();
	
	public FastValueLabeledGridPortrayal2D getFireStationPortrayal();
	
	public void setFireStationPortrayal(FastValueLabeledGridPortrayal2D fireStationPortrayal);
	
	public IntGrid2D getFireStationGrid();
	
	public void setFireStationGrid(IntGrid2D fireStationGrid);
	
	public String getName();
	
	public void addLabel(Int2D position, String label);
}
