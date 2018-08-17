package edu.usu.cs.mas.managedisaster.canvas;

import java.util.List;

import javax.inject.Inject;

import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

public class BuildingCanvasImpl implements BuildingCanvas{
  
  private static final int GRID_WIDTH = 100;
  private static final int GRID_HEIGHT = 100;
  
  
  private final String name = "Buildings";

  private FastValueLabeledGridPortrayal2D buildingsPortrayal;
  private IntGrid2D buildingsGrid;
  private List<BuildingEntity> buildingEntities;
  
  @Inject
  private BuildingPersister buildingPersister;
  
  public BuildingCanvasImpl(){ }
  
  @Override
  public void drawBuildings(){
    for( int x = 0 ; x < GRID_WIDTH ; x++ ) {
      for( int y = 0 ; y < GRID_HEIGHT ; y++ ) {
    	Long buildingId = getBuildingEntityId(x,y);  
        if(buildingId != null){
          buildingsGrid.field[x][y] = buildingId.intValue();
        }
      }
    }
  }
  
  private void setupLabels() {
  	if(buildingEntities == null){
      buildingEntities = buildingPersister.getAllBuildings();
    }
  	for(BuildingEntity buildingEntity : buildingEntities){
  	  Int2D labelIdCoordinate = new Int2D(buildingEntity.getLabelX(), buildingEntity.getLabelY()-2);
  	  buildingsPortrayal.addLabel(labelIdCoordinate, buildingEntity.getId().toString());
  		buildingsPortrayal.addLabel(buildingEntity.getLabelCoordinate(), buildingEntity.getName());
  	}
  }
  
  private Long getBuildingEntityId(int x, int y){
    if(buildingEntities == null){
      buildingEntities = buildingPersister.getAllBuildings();
    }
    for(BuildingEntity buildingEntity : buildingEntities){
      if(buildingEntity.contains(x, y)){
        return buildingEntity.getId();
      }
    }
    return null;
  }
  
  public int getBuildingId(int x, int y) {
  	return buildingsGrid.field[x][y];
  }
  
  @Override
  public boolean isBuildingCoordinate(int x, int y) {
    return buildingsGrid.field[x][y] > 0;
  }
  
  @Override
  public String getName(){
    return this.name;
  }

  @Override
  public IntGrid2D getBuildingsGrid() {
    return buildingsGrid;
  }

  @Override
  public void setBuildingsGrid(IntGrid2D buildingsGrid) {
    this.buildingsGrid = buildingsGrid;
  }

  @Override
  public FastValueLabeledGridPortrayal2D getBuildingsPortrayal(){
    return this.buildingsPortrayal;
  }
  
  @Override
  public void setBuildingsPortrayal(FastValueLabeledGridPortrayal2D buildingsPortrayal) {
    this.buildingsPortrayal = buildingsPortrayal;
    setupLabels();
  }

}
