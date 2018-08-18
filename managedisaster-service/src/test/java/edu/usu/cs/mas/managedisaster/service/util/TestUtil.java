package edu.usu.cs.mas.managedisaster.service.util;

import sim.util.MutableDouble2D;
import sim.util.MutableInt2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;

public class TestUtil {
  
  protected static final int WIDTH = 6;
  protected static final int LENGTH = 6;
  protected static final int HOTSPOTX = 3;
  protected static final int HOTSPOTY = 3;
  protected static final int MIN_HEAT = 2;
  protected static final int MAX_HEAT = 4;
  protected static final int MIN_SMOKE = 6;
  protected static final int MAX_SMOKE = 8;

  public FireEntity getFire() {
    return new FireEntity().withX(HOTSPOTX).withY(HOTSPOTY)
        .withEvaporationRate(0.99).withDiffusionRate(1.0)
        .withMinimumHeat(MIN_HEAT).withMaximumHeat(MAX_HEAT)
        .withMinimumSmoke(MIN_SMOKE).withMaximumSmoke(MAX_SMOKE)
        .withFusableSmokeAmount(new Long(MIN_SMOKE)).withFusableProbability(0.1)
        .withSmokeAmount(12L).withFireSpreadProbability(0.1)
        .withMinimumDiffuseProbability(0.2).withMaximumDiffuseProbability(0.8)
        .withMinimumEvaporationProbability(0.2).withMaximumEvaporationProbability(0.8)
        .withIncrementProbability(0.1);
  }
  
  public ForestEntity getForest() {
    GeometricShapeFactory gsf = new GeometricShapeFactory();
    gsf.setSize(6);
    gsf.setNumPoints(6);
    gsf.setBase(new Coordinate(0,0));
    Polygon rectangle = gsf.createRectangle();
    
    return new ForestEntity().withShape(rectangle);
  }
  
  public AgentPlayer getAgent(AgentPlayer agentPlayer) {
    if(agentPlayer == null) {
      agentPlayer = new AgentPlayer().withAgentModel(new AgentModel());
    }
    return agentPlayer.withCurrentLocation(new MutableDouble2D(0,0)).withSpeed(0.2)
        .withTargetLocation(new MutableInt2D(50,50))
        .withChemical(Chemical.WATER).withChemicalAmount(50.0).withMinimumFireProximity(5L);
  }
  
  public FireEntity createFire(int size, int numPoints, int baseX, int baseY) {
    ForestEntity forest = getForest();
    GeometricShapeFactory gsf = new GeometricShapeFactory();
    gsf.setSize(size);
    gsf.setNumPoints(numPoints);
    gsf.setBase(new Coordinate(baseX,baseY));
    Polygon rectangle = gsf.createRectangle();
    forest.withShape(rectangle);

    FireEntity fire = getFire();
    fire.setBurningForest(forest);
    
    return fire;
  }
}
