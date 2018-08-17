package edu.usu.cs.mas.managedisaster.canvas;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import sim.field.grid.IntGrid2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.util.MutableInt2D;
import edu.usu.cs.mas.managedisaster.common.RoadOrientation;
import edu.usu.cs.mas.managedisaster.entity.RoadEntity;
import edu.usu.cs.mas.managedisaster.persister.RoadPersister;

public class RoadCanvasImpl implements RoadCanvas{
  
  private static final Logger LOGGER = Logger.getLogger(RoadCanvasImpl.class);
  
  private final String name = "Roads";
  
  private FastValueGridPortrayal2D roadPortrayal;
  private IntGrid2D roadGrid;
  private List<RoadEntity> roadEntities;
  
  @Inject
  private RoadPersister roadPersister;
  
  public RoadCanvasImpl(){ }
  
  public RoadCanvasImpl(RoadPersister roadPersister){
    this.roadPersister = roadPersister;
  }
  
  @Override
  public void drawRoads(){
    if(roadEntities == null){
      roadEntities = roadPersister.getAllRoads();
    }
    for(RoadEntity roadEntity : roadEntities){
      int initX = roadEntity.getX();
      int initY = roadEntity.getY();
      int length = getLength(roadEntity);
      int width = getWidth(roadEntity);
      for(int x = initX; x<initX+length; x++){
        for(int y = initY; y<initY+width; y++){
          roadGrid.field[x][y] = roadEntity.getId().intValue();
        }
      }
    }
    LOGGER.debug("all the coordinated finish marking");
  }
  
  private int getLength(RoadEntity roadEntity){
    if(roadEntity.getOrientation() == RoadOrientation.HORIZONTAL){
      return roadEntity.getLength();
    }
    return roadEntity.getWidth();
  }
  
  private int getWidth(RoadEntity roadEntity){
    if(roadEntity.getOrientation() == RoadOrientation.HORIZONTAL){
      return roadEntity.getWidth();
    }
    return roadEntity.getLength();
  }
  
  @Override
  public IntGrid2D getRoadGrid() {
    return roadGrid;
  }

  @Override
  public void setRoadGrid(IntGrid2D roadGrid) {
    this.roadGrid = roadGrid;
  }

  @Override
  public FastValueGridPortrayal2D getRoadPortrayal(){
    return this.roadPortrayal;
  }
  
  @Override
  public void setRoadPortrayal(FastValueGridPortrayal2D roadPortrayal) {
    this.roadPortrayal = roadPortrayal;
  }


  @Override
  public String getName(){
    return this.name;
  }
  
  @Override
  public boolean isRoadCoordinate(int x, int y){
    return roadGrid.field[x][y] > 0;
  }
  
  public int getRoadId(MutableInt2D location) {
  	return roadGrid.field[location.x][location.y];
  }

}
