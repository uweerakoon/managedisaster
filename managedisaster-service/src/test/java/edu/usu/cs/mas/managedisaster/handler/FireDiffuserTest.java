package edu.usu.cs.mas.managedisaster.handler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.anyDouble;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinter;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinterImpl;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.handler.FireDiffuser;
import edu.usu.cs.mas.managedisaster.persister.ForestPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.service.util.TestUtil;

public class FireDiffuserTest extends TestUtil{

  @SuppressWarnings("unused")
  private static final Logger LOGGER = Logger.getLogger(FireDiffuserTest.class);
  private static final String FOLDER_NAME = "/opt/managedisaster/report/";
  private static final String FILE_NAME = "FireGridStatus.csv";
  
  private FireGridCsvPrinter fireGridCsvPrinter = new FireGridCsvPrinterImpl();
  private DoubleGrid2D currentFireGrid;
  private DoubleGrid2D newFireGrid;
  private DoubleGrid2D currentSmokeGrid;
  private DoubleGrid2D newSmokeGrid;
  private IntGrid2D forestGrid;
  private FireEntity fire;
  
  @Mock
  private FirePersister firePersister;
  @Mock
  private FireCanvas fireCanvas;
  @Mock
  private ForestPersister forestPersister;
  @Mock
  private MersenneTwisterFast random;
  @Mock
  private ForestCanvas forestCanvas;
  
  private FireDiffuser fireDiffuser;
  
  @Before
  public void setup(){
    initMocks(this);
    fireDiffuser = new FireDiffuser(fireGridCsvPrinter, fireCanvas, firePersister, forestPersister, random, forestCanvas);
    fire = getFire();
    currentFireGrid = new DoubleGrid2D(WIDTH, LENGTH, 0);
    newFireGrid = new DoubleGrid2D(WIDTH, LENGTH, 0);
    currentSmokeGrid = new DoubleGrid2D(WIDTH, LENGTH, 0);
    newSmokeGrid = new DoubleGrid2D(WIDTH, LENGTH, 0);
    
    when(fireCanvas.getCurrentFireGrid()).thenReturn(currentFireGrid);
    when(fireCanvas.getNewFireGrid()).thenReturn(newFireGrid);
    when(fireCanvas.getCurrentSmokeGrid()).thenReturn(currentSmokeGrid);
    when(fireCanvas.getNewSmokeGrid()).thenReturn(newSmokeGrid);
    when(firePersister.getActiveFires()).thenReturn(Arrays.asList(fire));
    when(forestPersister.getForest(0L)).thenReturn(getForest());
  }
  
  @Test
  public void testDiffuseFireZeroRadius() {
    when(random.nextBoolean(anyDouble())).thenReturn(true);
    when(random.nextDouble()).thenReturn(0.9);
    currentSmokeGrid.field[fire.getX()][fire.getY()] = 20.0;
    
    fireDiffuser.diffuseFire();
    
    assertNotNull(fire.getBurningForest());
    assertNotEquals(0.0, newFireGrid.field[HOTSPOTX][HOTSPOTY]);
    assertThat((double)MIN_HEAT, lessThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
    assertThat((double)MAX_HEAT, greaterThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
  }
  
  @Test
  public void testDiffuseFireNonZeroRadius() {
    ForestEntity forest = getForest();
    forestGrid = new IntGrid2D(WIDTH, LENGTH, 0);
    fire.setBurningForest(forest);
    fire.setFireRadius(1);
    when(forestCanvas.getForestsGrid()).thenReturn(forestGrid);
    when(random.nextBoolean(anyDouble())).thenReturn(true);
    when(random.nextDouble()).thenReturn(0.9);
    when(random.nextBoolean(anyDouble())).thenReturn(true);
    currentFireGrid.field[HOTSPOTX][HOTSPOTY] = 2.5;
    forestGrid.field[HOTSPOTX][HOTSPOTY] = 1;
    currentSmokeGrid.field[HOTSPOTX][HOTSPOTY] = 1.0;
    
    fireDiffuser.diffuseFire();
    
    assertNotNull(fire.getBurningForest());
    assertNotEquals(0.0, newFireGrid.field[HOTSPOTX][HOTSPOTY]);
    assertThat(0.0, lessThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
    assertThat((double)MAX_HEAT, greaterThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
    
  }
  
  @Test
  public void testDiffuseSmokeForestBurning() {
    ForestEntity forest = getForest();
    fire.setBurningForest(forest);
    currentFireGrid.field[HOTSPOTX][HOTSPOTY] = 2.5;
    when(random.nextBoolean(anyDouble())).thenReturn(true);
    fireDiffuser.diffuseFire();
    
    assertThat((double)MIN_SMOKE, lessThanOrEqualTo(newSmokeGrid.field[HOTSPOTX][HOTSPOTY]));
  }

//  @Test
  public void testCsvHeadersFireForestBurning() {
    ForestEntity forest = getForest();
    fire.setBurningForest(forest);
    fireDiffuser.setStoreData(true);
    fireDiffuser.diffuseFire();
    fireGridCsvPrinter.closeFile();
    File file = new File(FOLDER_NAME + FILE_NAME);

    assertTrue(file.exists());
    assertTrue(file.length() > 0);
    assertThat((double)MIN_HEAT, lessThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
    assertTrue(file.delete());
  }
  
//  @Test
  public void testCsvRecordsFireForestBurning() {
    ForestEntity forest = getForest();
    fire.setBurningForest(forest);
    fireDiffuser.setStoreData(true);
    fireGridCsvPrinter.printRemaining();
    currentFireGrid.field[HOTSPOTX][HOTSPOTY] = 2.5;
    fireDiffuser.diffuseFire();
    fireGridCsvPrinter.closeFile();
    File file = new File(FOLDER_NAME + FILE_NAME);

    assertTrue(file.exists());
    assertTrue(file.length() > 0);
    assertThat((double)MIN_HEAT, lessThanOrEqualTo(newFireGrid.field[HOTSPOTX][HOTSPOTY]));
    assertTrue(file.delete());
  }
}
