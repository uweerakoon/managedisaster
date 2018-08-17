package edu.usu.cs.mas.managedisaster.persister;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.common.Originator;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.message.Severity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import sim.engine.SimState;
import sim.util.MutableInt2D;

public class FirePersisterImpl implements FirePersister{

  private static final Logger LOGGER = Logger.getLogger(FirePersisterImpl.class);

  private static final boolean ENABLE = true;

  private static final long FIRE_DELAY = 5;

  private EntityManager entityManager;

  @Inject
  private HibernateUtil hibernateUtil;

  // Helps to keep the iteration count
  private SimState simState;

  public FirePersisterImpl() { }

  public FirePersisterImpl(HibernateUtil hibernateUtil) { 
    this.hibernateUtil = hibernateUtil;
  }

  @Override
  public List<FireEntity> getAllFires() {
    entityManager = hibernateUtil.getEntityManager();
    String strQuery = "select f from FireEntity f";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<FireEntity> allFires = query.getResultList();
    return allFires;
  }

  @Override
  public List<FireEntity> getActiveFires() {
    entityManager = hibernateUtil.getEntityManager();
    long currentTime = (long)simState.schedule.getTime();
    String strQuery = "select f from FireEntity f where f.startingTime <= "+currentTime
        +" and enable = true and active = true";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<FireEntity> activeFires = query.getResultList();
    return activeFires;
  }
  
  @Override
  public List<FireEntity> getUnallcoatedActiveFires() {
    entityManager = hibernateUtil.getEntityManager();
    long currentTime = (long)simState.schedule.getTime();
    String strQuery = "select f from FireEntity f where f.startingTime <= "+currentTime
        +" and enable = true and active = true and coalition is null";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<FireEntity> activeFires = query.getResultList();
    return activeFires;
  }

  @Override
  public FireEntity getFire(Long id){
    entityManager = hibernateUtil.getEntityManager();
    FireEntity fire = entityManager.find(FireEntity.class, id);
    return fire;
  }

  @Override
  public void saveFire(FireEntity fire) {
    entityManager = hibernateUtil.getEntityManager();
    entityManager.persist(fire);
    hibernateUtil.commit();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void cleanup() {
    entityManager = hibernateUtil.getEntityManager();
    // 1. Remove all twitter created fires
    String strQuery = "select f from FireEntity f where f.originator = '"+Originator.SYSTEM+"'";
    Query query = entityManager.createQuery(strQuery);
    List<FireEntity> systemFires = query.getResultList();
    for(FireEntity systemFire : systemFires) {
      entityManager.remove(systemFire);
    }

    // 2. Set back the disabled fires
    strQuery = "select f from FireEntity f where f.enable = false";
    query = entityManager.createQuery(strQuery);
    List<FireEntity> disableFires = query.getResultList();
    for(FireEntity disableFire : disableFires) {
      disableFire.setEnable(true);
      entityManager.persist(disableFire);
    }
    
    // 3. Remove the coalitions assigned to the fire
    strQuery = "select f from FireEntity f where coalition is not null";
    query = entityManager.createQuery(strQuery);
    List<FireEntity> coalitionedFires = query.getResultList();
    for(FireEntity coalitionedFire : coalitionedFires) {
      coalitionedFire.setCoalition(null);
      entityManager.persist(coalitionedFire);
    }
    
    hibernateUtil.commit();
  }

  // We cannot use agent entity because at the simulation level agent model has all the up-to-date values
  @Override
  public List<FireEntity> getCloseFires(double x, double y, long vicinity) { 
    List<FireEntity> closeFires = new ArrayList<>();
    List<FireEntity> fires = getUnallcoatedActiveFires();
    for(FireEntity fire : fires) {
      // TODO need to check whether the fire is active, fire.startTime >= current iteration
      if(!fire.isEnable() || fire.getStartingTime() > simState.schedule.getTime()) {
        continue;
      }
      double distance = Math.sqrt(
        ((fire.getX()-x)*(fire.getX()-x))
        +((fire.getY()-y)*(fire.getY()-y)));
      if(distance <= vicinity) {
        closeFires.add(fire);
      }
    }
    return closeFires;
  }

  @Override
  public void setSimState(SimState simState) {
    this.simState = simState;
  }

  @Override
  public FireEntity generateFire(Message message) {
    BuildingEntity building = message.getBuilding();
    MutableInt2D fireLocation = building.getRandomCoordinate();

    FireEntity fire = new FireEntity().withX(fireLocation.x).withY(fireLocation.y).withBurningBuilding(building);

    switch(message.getSeverity()) {
      case MILD:
        fire = getMildFire(fire);
        break;
      case NORMAL:
        fire = getNormalFire(fire);
        break;
      case SEVERE:
        fire = getSevereFire(fire);
    }
    fire.withOriginator(Originator.SYSTEM)
    .withStartingTime(/*(long)simState.schedule.getTime()+FIRE_DELAY*/0l);

    saveFire(fire);

    LOGGER.info("Saving new fire. Fire: "+fire);

    return fire;
  }

  private static final int MILD_MIN_HEAT = 6000;
  private static final int MILD_MAX_HEAT = 10000;
  private static final int MILD_MIN_SMOKE = 450;
  private static final int MILD_MAX_SMOKE = 100000;
  private static final double MILD_SMOKE_EVAPORATION_RATE = 0.01;
  private static final double MILD_FIRE_EVAPORATION_RATE = 0.993;
  private static final double MILD_FIRE_DIFFUSION_RATE = 1.0;
  private static final double MILD_FIRE_SPREAD_PROBABILITY = 0.06;
  private static final double MILD_FIRE_MINIMUM_DIFFUSE_PROBABILITY = 0.3;
  private static final double MILD_FIRE_MAXIMUM_DIFFUSE_PROBABILITY = 0.4;
  private static final double MILD_FIRE_MINIMUM_EVAPORATION_PROBABILITY = 0.1;
  private static final double MILD_FIRE_MAXIMUM_EVAPORATION_PROBABILITY = 0.2;
  private static final double MILD_FIRE_INCREMENT_PROBABILITY = 0.51;
  private static final long MILD_FUSABLE_SMOKE_AMOUNT = 10000L;
  private static final double MILD_FUSABLE_PROBABILITY = 0.3;
  private static final long MILD_SMOKE_AMOUNT = 900L;

  private FireEntity getMildFire(FireEntity fire) {
    fire.withMinimumHeat(MILD_MIN_HEAT).withMaximumHeat(MILD_MAX_HEAT)
    .withMinimumSmoke(MILD_MIN_SMOKE).withMaximumSmoke(MILD_MAX_SMOKE)
    .withSmokeEvaporationRate(MILD_SMOKE_EVAPORATION_RATE).withEvaporationRate(MILD_FIRE_EVAPORATION_RATE)
    .withDiffusionRate(MILD_FIRE_DIFFUSION_RATE).withFireSpreadProbability(MILD_FIRE_SPREAD_PROBABILITY)
    .withMinimumDiffuseProbability(MILD_FIRE_MINIMUM_DIFFUSE_PROBABILITY)
    .withMaximumDiffuseProbability(MILD_FIRE_MAXIMUM_DIFFUSE_PROBABILITY)
    .withMinimumEvaporationProbability(MILD_FIRE_MINIMUM_EVAPORATION_PROBABILITY)
    .withMaximumEvaporationProbability(MILD_FIRE_MAXIMUM_EVAPORATION_PROBABILITY)
    .withIncrementProbability(MILD_FIRE_INCREMENT_PROBABILITY)
    .withFusableSmokeAmount(MILD_FUSABLE_SMOKE_AMOUNT)
    .withFusableProbability(MILD_FUSABLE_PROBABILITY)
    .withSmokeAmount(MILD_SMOKE_AMOUNT)
    .withEnable(ENABLE)
    .withActive(true)
    .withSeverity(Severity.MILD);
    return fire;
  }

  private static final int NORMAL_MIN_HEAT = 9000;
  private static final int NORMAL_MAX_HEAT = 13000;
  private static final int NORMAL_MIN_SMOKE = 1000;
  private static final int NORMAL_MAX_SMOKE = 100000;
  private static final double NORMAL_SMOKE_EVAPORATION_RATE = 0.005;
  private static final double NORMAL_FIRE_EVAPORATION_RATE = 0.882;
  private static final double NORMAL_FIRE_DIFFUSION_RATE = 1.0;
  private static final double NORMAL_FIRE_SPREAD_PROBABILITY = 0.12;
  private static final double NORMAL_FIRE_MINIMUM_DIFFUSE_PROBABILITY = 0.4;
  private static final double NORMAL_FIRE_MAXIMUM_DIFFUSE_PROBABILITY = 0.5;
  private static final double NORMAL_FIRE_MINIMUM_EVAPORATION_PROBABILITY = 0.04;
  private static final double NORMAL_FIRE_MAXIMUM_EVAPORATION_PROBABILITY = 0.06;
  private static final double NORMAL_FIRE_INCREMENT_PROBABILITY = 0.61;
  private static final long NORMAL_FUSABLE_SMOKE_AMOUNT = 10000L;
  private static final double NORMAL_FUSABLE_PROBABILITY = 0.5;
  private static final long NORMAL_SMOKE_AMOUNT = 900L;

  private FireEntity getNormalFire(FireEntity fire) {
    fire.withMinimumHeat(NORMAL_MIN_HEAT).withMaximumHeat(NORMAL_MAX_HEAT)
    .withMinimumSmoke(NORMAL_MIN_SMOKE).withMaximumSmoke(NORMAL_MAX_SMOKE)
    .withSmokeEvaporationRate(NORMAL_SMOKE_EVAPORATION_RATE).withEvaporationRate(NORMAL_FIRE_EVAPORATION_RATE)
    .withDiffusionRate(NORMAL_FIRE_DIFFUSION_RATE).withFireSpreadProbability(NORMAL_FIRE_SPREAD_PROBABILITY)
    .withMinimumDiffuseProbability(NORMAL_FIRE_MINIMUM_DIFFUSE_PROBABILITY)
    .withMaximumDiffuseProbability(NORMAL_FIRE_MAXIMUM_DIFFUSE_PROBABILITY)
    .withMinimumEvaporationProbability(NORMAL_FIRE_MINIMUM_EVAPORATION_PROBABILITY)
    .withMaximumEvaporationProbability(NORMAL_FIRE_MAXIMUM_EVAPORATION_PROBABILITY)
    .withIncrementProbability(NORMAL_FIRE_INCREMENT_PROBABILITY)
    .withFusableSmokeAmount(NORMAL_FUSABLE_SMOKE_AMOUNT)
    .withFusableProbability(NORMAL_FUSABLE_PROBABILITY)
    .withSmokeAmount(NORMAL_SMOKE_AMOUNT)
    .withEnable(ENABLE)
    .withActive(true)
    .withSeverity(Severity.NORMAL);
    return fire;
  }

  private static final int SEVERE_MIN_HEAT = 12000;
  private static final int SEVERE_MAX_HEAT = 16000;
  private static final int SEVERE_MIN_SMOKE = 1500;
  private static final int SEVERE_MAX_SMOKE = 100000;
  private static final double SEVERE_SMOKE_EVAPORATION_RATE = 0.001;
  private static final double SEVERE_FIRE_EVAPORATION_RATE = 0.771;
  private static final double SEVERE_FIRE_DIFFUSION_RATE = 1.0;
  private static final double SEVERE_FIRE_SPREAD_PROBABILITY = 0.18;
  private static final double SEVERE_FIRE_MINIMUM_DIFFUSE_PROBABILITY = 0.5;
  private static final double SEVERE_FIRE_MAXIMUM_DIFFUSE_PROBABILITY = 0.6;
  private static final double SEVERE_FIRE_MINIMUM_EVAPORATION_PROBABILITY = 0.01;
  private static final double SEVERE_FIRE_MAXIMUM_EVAPORATION_PROBABILITY = 0.02;
  private static final double SEVERE_FIRE_INCREMENT_PROBABILITY = 0.71;
  private static final long SEVERE_FUSABLE_SMOKE_AMOUNT = 10000L;
  private static final double SEVERE_FUSABLE_PROBABILITY = 0.7;
  private static final long SEVERE_SMOKE_AMOUNT = 900L;

  private FireEntity getSevereFire(FireEntity fire) {
    fire.withMinimumHeat(SEVERE_MIN_HEAT).withMaximumHeat(SEVERE_MAX_HEAT)
    .withMinimumSmoke(SEVERE_MIN_SMOKE).withMaximumSmoke(SEVERE_MAX_SMOKE)
    .withSmokeEvaporationRate(SEVERE_SMOKE_EVAPORATION_RATE).withEvaporationRate(SEVERE_FIRE_EVAPORATION_RATE)
    .withDiffusionRate(SEVERE_FIRE_DIFFUSION_RATE).withFireSpreadProbability(SEVERE_FIRE_SPREAD_PROBABILITY)
    .withMinimumDiffuseProbability(SEVERE_FIRE_MINIMUM_DIFFUSE_PROBABILITY)
    .withMaximumDiffuseProbability(SEVERE_FIRE_MAXIMUM_DIFFUSE_PROBABILITY)
    .withMinimumEvaporationProbability(SEVERE_FIRE_MINIMUM_EVAPORATION_PROBABILITY)
    .withMaximumEvaporationProbability(SEVERE_FIRE_MAXIMUM_EVAPORATION_PROBABILITY)
    .withIncrementProbability(SEVERE_FIRE_INCREMENT_PROBABILITY)
    .withFusableSmokeAmount(SEVERE_FUSABLE_SMOKE_AMOUNT)
    .withFusableProbability(SEVERE_FUSABLE_PROBABILITY)
    .withSmokeAmount(SEVERE_SMOKE_AMOUNT)
    .withEnable(ENABLE)
    .withActive(true)
    .withSeverity(Severity.SEVERE);
    return fire;
  }
}
