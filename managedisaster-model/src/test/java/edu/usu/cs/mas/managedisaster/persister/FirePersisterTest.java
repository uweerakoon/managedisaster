package edu.usu.cs.mas.managedisaster.persister;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import sim.engine.Schedule;
import sim.engine.SimState;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.message.MessageType;
import edu.usu.cs.mas.managedisaster.message.Severity;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;

public class FirePersisterTest {
  
  private static final String WALMART = "pines";
  private static final long START_TIME = 1L;
  
  private HibernateUtil hibernateUtil;
  private FirePersisterImpl firePersister;
  private ForestPersisterImpl forestPersister;
  
  @Mock
  private SimState simState;
  @Mock
  private Schedule schedule;
  
  public FirePersisterTest() {
    hibernateUtil = new HibernateUtilImpl();
    firePersister = new FirePersisterImpl(hibernateUtil);
    forestPersister = new ForestPersisterImpl(hibernateUtil);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetFire() {
    FireEntity fire = firePersister.getFire(1L);
    assertEquals(70, fire.getX().intValue());
    assertEquals(35, fire.getY().intValue());
  }

//  @Test
  public void testGetAllFires() {
    List<FireEntity> fires = firePersister.getAllFires();
    assertEquals(1, fires.size());
  }

  @Test
  public void testGenerateMildFire() {
    ForestEntity forest = forestPersister.getForest(WALMART);
    Message message = new Message().withForest(forest).withMessageType(MessageType.FIRE)
                        .withSeverity(Severity.MILD);
    simState.schedule = schedule;
    firePersister.setSimState(simState);
    FireEntity fire = firePersister.generateFire(message);
    assertEquals(FIRE_DELAY, fire.getStartingTime(), THRESHOLD);
    assertMildFire(fire);
  }
  
  @Test
  public void testGenerateNormalFire() {
    ForestEntity forest = forestPersister.getForest(WALMART);
    Message message = new Message().withForest(forest).withMessageType(MessageType.FIRE).withSeverity(Severity.NORMAL);
    simState.schedule = schedule;
    firePersister.setSimState(simState);
    FireEntity fire = firePersister.generateFire(message);
    assertEquals(FIRE_DELAY, fire.getStartingTime(), THRESHOLD);
    assertNormalFire(fire);
  }
  
  @Test
  public void testGenerateSevereFire() {
    ForestEntity forest = forestPersister.getForest(WALMART);
    Message message = new Message().withForest(forest).withMessageType(MessageType.FIRE).withSeverity(Severity.SEVERE);
    simState.schedule = schedule;
    firePersister.setSimState(simState);
    FireEntity fire = firePersister.generateFire(message);
    assertEquals(FIRE_DELAY, fire.getStartingTime(), THRESHOLD);
    assertSevereFire(fire);
  }
  
  private void assertMildFire(FireEntity fire) {
    assertNotNull(fire.getId());
    assertEquals(MILD_MIN_HEAT, fire.getMinimumHeat(), THRESHOLD);
    assertEquals(MILD_MAX_HEAT, fire.getMaximumHeat(), THRESHOLD);
    assertEquals(MILD_MIN_SMOKE, fire.getMinimumSmoke(), THRESHOLD);
    assertEquals(MILD_MAX_SMOKE, fire.getMaximumSmoke(), THRESHOLD);
    assertEquals(MILD_SMOKE_EVAPORATION_RATE, fire.getSmokeEvaporationRate(), THRESHOLD);
    assertEquals(MILD_FIRE_EVAPORATION_RATE, fire.getEvaporationRate(), THRESHOLD);
    assertEquals(MILD_FIRE_DIFFUSION_RATE, fire.getDiffusionRate(), THRESHOLD);
    assertEquals(MILD_FIRE_SPREAD_PROBABILITY, fire.getFireSpreadProbability(), THRESHOLD);
    assertEquals(MILD_FIRE_MINIMUM_DIFFUSE_PROBABILITY, fire.getMinimumDiffuseProbability(), THRESHOLD);
    assertEquals(MILD_FIRE_MAXIMUM_DIFFUSE_PROBABILITY, fire.getMaximumDiffuseProbability(), THRESHOLD);
    assertEquals(MILD_FIRE_MINIMUM_EVAPORATION_PROBABILITY, fire.getMinimumEvaporationProbability(), THRESHOLD);
    assertEquals(MILD_FIRE_MAXIMUM_EVAPORATION_PROBABILITY, fire.getMaximumEvaporationProbability(), THRESHOLD);
    assertEquals(MILD_FIRE_INCREMENT_PROBABILITY, fire.getIncrementProbability(), THRESHOLD);
    assertEquals(MILD_FUSABLE_SMOKE_AMOUNT, fire.getFusableSmokeAmount(), THRESHOLD);
    assertEquals(MILD_FUSABLE_PROBABILITY, fire.getFusableProbability(), THRESHOLD);
    assertEquals(MILD_SMOKE_AMOUNT, fire.getSmokeAmount(), THRESHOLD);
    assertEquals(ENABLE, fire.isEnable());
  }
  
  private void assertNormalFire(FireEntity fire) {
    assertNotNull(fire.getId());
    assertEquals(NORMAL_MIN_HEAT, fire.getMinimumHeat(), THRESHOLD);
    assertEquals(NORMAL_MAX_HEAT, fire.getMaximumHeat(), THRESHOLD);
    assertEquals(NORMAL_MIN_SMOKE, fire.getMinimumSmoke(), THRESHOLD);
    assertEquals(NORMAL_MAX_SMOKE, fire.getMaximumSmoke(), THRESHOLD);
    assertEquals(NORMAL_SMOKE_EVAPORATION_RATE, fire.getSmokeEvaporationRate(), THRESHOLD);
    assertEquals(NORMAL_FIRE_EVAPORATION_RATE, fire.getEvaporationRate(), THRESHOLD);
    assertEquals(NORMAL_FIRE_DIFFUSION_RATE, fire.getDiffusionRate(), THRESHOLD);
    assertEquals(NORMAL_FIRE_SPREAD_PROBABILITY, fire.getFireSpreadProbability(), THRESHOLD);
    assertEquals(NORMAL_FIRE_MINIMUM_DIFFUSE_PROBABILITY, fire.getMinimumDiffuseProbability(), THRESHOLD);
    assertEquals(NORMAL_FIRE_MAXIMUM_DIFFUSE_PROBABILITY, fire.getMaximumDiffuseProbability(), THRESHOLD);
    assertEquals(NORMAL_FIRE_MINIMUM_EVAPORATION_PROBABILITY, fire.getMinimumEvaporationProbability(), THRESHOLD);
    assertEquals(NORMAL_FIRE_MAXIMUM_EVAPORATION_PROBABILITY, fire.getMaximumEvaporationProbability(), THRESHOLD);
    assertEquals(NORMAL_FIRE_INCREMENT_PROBABILITY, fire.getIncrementProbability(), THRESHOLD);
    assertEquals(NORMAL_FUSABLE_SMOKE_AMOUNT, fire.getFusableSmokeAmount(), THRESHOLD);
    assertEquals(NORMAL_FUSABLE_PROBABILITY, fire.getFusableProbability(), THRESHOLD);
    assertEquals(NORMAL_SMOKE_AMOUNT, fire.getSmokeAmount(), THRESHOLD);
    assertEquals(ENABLE, fire.isEnable());
  }
  
  private void assertSevereFire(FireEntity fire) {
    assertNotNull(fire.getId());
    assertEquals(SEVERE_MIN_HEAT, fire.getMinimumHeat(), THRESHOLD);
    assertEquals(SEVERE_MAX_HEAT, fire.getMaximumHeat(), THRESHOLD);
    assertEquals(SEVERE_MIN_SMOKE, fire.getMinimumSmoke(), THRESHOLD);
    assertEquals(SEVERE_MAX_SMOKE, fire.getMaximumSmoke(), THRESHOLD);
    assertEquals(SEVERE_SMOKE_EVAPORATION_RATE, fire.getSmokeEvaporationRate(), THRESHOLD);
    assertEquals(SEVERE_FIRE_EVAPORATION_RATE, fire.getEvaporationRate(), THRESHOLD);
    assertEquals(SEVERE_FIRE_DIFFUSION_RATE, fire.getDiffusionRate(), THRESHOLD);
    assertEquals(SEVERE_FIRE_SPREAD_PROBABILITY, fire.getFireSpreadProbability(), THRESHOLD);
    assertEquals(SEVERE_FIRE_MINIMUM_DIFFUSE_PROBABILITY, fire.getMinimumDiffuseProbability(), THRESHOLD);
    assertEquals(SEVERE_FIRE_MAXIMUM_DIFFUSE_PROBABILITY, fire.getMaximumDiffuseProbability(), THRESHOLD);
    assertEquals(SEVERE_FIRE_MINIMUM_EVAPORATION_PROBABILITY, fire.getMinimumEvaporationProbability(), THRESHOLD);
    assertEquals(SEVERE_FIRE_MAXIMUM_EVAPORATION_PROBABILITY, fire.getMaximumEvaporationProbability(), THRESHOLD);
    assertEquals(SEVERE_FIRE_INCREMENT_PROBABILITY, fire.getIncrementProbability(), THRESHOLD);
    assertEquals(SEVERE_FUSABLE_SMOKE_AMOUNT, fire.getFusableSmokeAmount(), THRESHOLD);
    assertEquals(SEVERE_FUSABLE_PROBABILITY, fire.getFusableProbability(), THRESHOLD);
    assertEquals(SEVERE_SMOKE_AMOUNT, fire.getSmokeAmount(), THRESHOLD);
    assertEquals(ENABLE, fire.isEnable());
  }
  
  private static final long FIRE_DELAY = 5;
  private static final boolean ENABLE = true;
  
  private static final double THRESHOLD = .001;
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
}
