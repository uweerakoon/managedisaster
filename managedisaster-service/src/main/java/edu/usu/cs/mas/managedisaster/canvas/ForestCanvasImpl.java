package edu.usu.cs.mas.managedisaster.canvas;

import java.util.List;

import javax.inject.Inject;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.persister.ForestPersister;
import edu.usu.cs.mas.managedisaster.portrayal.FastValueLabeledGridPortrayal2D;
import sim.field.grid.IntGrid2D;
import sim.util.Int2D;

public class ForestCanvasImpl implements ForestCanvas{
  
  private static final int GRID_WIDTH = 100;
  private static final int GRID_HEIGHT = 100;
  
  private final String name = "Forests";
  private MersenneTwisterFast random = new MersenneTwisterFast(System.currentTimeMillis());

  private FastValueLabeledGridPortrayal2D forestsPortrayal;
  private IntGrid2D forestsGrid;
  private List<ForestEntity> forestEntities;
  
  @Inject
  private ForestPersister forestPersister;
  
  public ForestCanvasImpl(){ }
  
  @Override
  public void drawForests(){
    for( int x = 0 ; x < GRID_WIDTH ; x++ ) {
      for( int y = 0 ; y < GRID_HEIGHT ; y++ ) {
    	boolean isForest = isForestEntity(x,y);  
        if(isForest){
          forestsGrid.field[x][y] = 1; // random.nextInt(6);
        }
      }
    }
  }
  
  private void setupLabels() {
  	if(forestEntities == null){
      forestEntities = forestPersister.getAllForests();
    }
  	for(ForestEntity forestEntity : forestEntities){
  	  Int2D labelIdCoordinate = new Int2D(forestEntity.getLabelX(), forestEntity.getLabelY()-2);
  	  forestsPortrayal.addLabel(labelIdCoordinate, forestEntity.getId().toString());
  		forestsPortrayal.addLabel(forestEntity.getLabelCoordinate(), forestEntity.getName());
  	}
  }
  
  private boolean isForestEntity(int x, int y){
    if(forestEntities == null){
      forestEntities = forestPersister.getAllForests();
    }
    for(ForestEntity forestEntity : forestEntities){
      if(forestEntity.contains(x, y)){
        return true;
      }
    }
    return false;
  }
  
  @Override
  public int getForestId(int x, int y) {
  	return forestsGrid.field[x][y];
  }
  
  @Override
  public boolean isForestCoordinate(int x, int y) {
    return forestsGrid.field[x][y] > 0;
  }
  
  @Override
  public String getName(){
    return this.name;
  }

  @Override
  public IntGrid2D getForestsGrid() {
    return forestsGrid;
  }

  @Override
  public void setForestsGrid(IntGrid2D forestsGrid) {
    this.forestsGrid = forestsGrid;
  }

  @Override
  public FastValueLabeledGridPortrayal2D getForestsPortrayal(){
    return this.forestsPortrayal;
  }
  
  @Override
  public void setForestsPortrayal(FastValueLabeledGridPortrayal2D forestsPortrayal) {
    this.forestsPortrayal = forestsPortrayal;
    setupLabels();
  }

}
