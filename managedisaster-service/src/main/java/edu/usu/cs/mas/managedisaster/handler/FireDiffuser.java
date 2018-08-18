package edu.usu.cs.mas.managedisaster.handler;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.canvas.BuildingCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinter;
import edu.usu.cs.mas.managedisaster.csv.FireGridState;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.persister.ForestPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class FireDiffuser implements Steppable {

  private static final Logger LOGGER = Logger.getLogger(FireDiffuser.class);
  private static final long serialVersionUID = 1;

  private MersenneTwisterFast random;
  private int iteration = 0;
  private boolean storeData;

  @Inject
  private FireCanvas fireCanvas;
  @Inject
  private BuildingCanvas buildingCanvas; 
  @Inject
  private FirePersister firePersister;
  @Inject
  private ForestPersister buildingPersister;
  @Inject
  private FireGridCsvPrinter fireGridCsvPrinter;

  public FireDiffuser() { 
    random = new MersenneTwisterFast(System.currentTimeMillis());
  }

  public FireDiffuser(FireGridCsvPrinter fireGridCsvPrinter, 
    FireCanvas fireCanvas, FirePersister firePersister,
    ForestPersister buildingPersister, MersenneTwisterFast random,
    BuildingCanvas buildingCanvas) {
    this();
    this.fireGridCsvPrinter = fireGridCsvPrinter;
    this.fireCanvas = fireCanvas;
    this.firePersister = firePersister;
    this.buildingPersister = buildingPersister;
    this.random = random;
    this.buildingCanvas = buildingCanvas;
  }

  @Override
  public void step(SimState state){
    this.iteration++;
    diffuseFire();
    fireCanvas.getCurrentFireGrid().setTo(fireCanvas.getNewFireGrid());
    fireCanvas.getCurrentSmokeGrid().setTo(fireCanvas.getNewSmokeGrid());
  }

  public void diffuseFire(){

    List<FireEntity> fires = firePersister.getActiveFires();

    if(CollectionUtils.isEmpty(fires)) {
      return;
    }

    for(FireEntity fire : fires) {
      // Current value is helping for statistics
      fire.setCurrentFireValue(0.0);
      fire.setCurrentSmokeValue(0.0);
      if(fire.getBurningForest() == null) {
        setupBurningBuilding(fire);
      }

      handleSmoke(fire);
      if(fire.getSmokeRadius() > 0) {
        handleFire(fire);
      }
    }
  }

  private void handleSmoke(FireEntity fire) {
    int radius = fire.getSmokeRadius();
    if(radius == 0) {
      setupSmoke(fire);
      fire.setSmokeRadius(1);
      return;
    }
    smokeBuilding(fire);
  }

  private void smokeBuilding(FireEntity fire) {
    ForestEntity burningBuilding = fire.getBurningForest();
    int minX = burningBuilding.getMinX();
    int maxX = burningBuilding.getMaxX();
    int minY = burningBuilding.getMinY();
    int maxY = burningBuilding.getMaxY();

    int[][] buildingField = buildingCanvas.getBuildingsGrid().field;
    double[][] currentSmokeGridField = fireCanvas.getCurrentSmokeGrid().field;
    double[][] newSmokeGridField = fireCanvas.getNewSmokeGrid().field;

    double[] previousSmokeXAxis = currentSmokeGridField[minX];
    double[] currentSmokeXAxis = currentSmokeGridField[minX];
    double[] nextSmokeXAxis;

    int[] previousBuildingXAxis = buildingField[minX];
    int[] currentBuildingXAxis = buildingField[minX];
    int[] nextBuildingXAxis;

    double[] put;

    double totalSmoke = 0.0;

    for(int x = minX; x < maxX; x++) {
      nextSmokeXAxis = x + 1 < maxX ? currentSmokeGridField[x+1] : currentSmokeXAxis; 
      nextBuildingXAxis = x + 1 < maxX ? buildingField[x+1] : currentBuildingXAxis;

      put = newSmokeGridField[x];
      int yMinus1 = minY;

      for(int y = minY; y < maxY; y++) {
        if(currentBuildingXAxis[y] == 0) {
          yMinus1 = y;
          continue;
        }
        int yPlus1 = y + 1 < maxY ? y+1 : y;
        double average = 0.0;
        double currentValue = currentSmokeXAxis[y];

        if(currentBuildingXAxis[yMinus1] == 0 || currentSmokeXAxis[yMinus1] == 0.0) { // x, y-1
          average += currentValue;
        }
        if(currentBuildingXAxis[yPlus1] == 0 || currentSmokeXAxis[yPlus1] == 0.0) { // x, y+1
          average += currentValue;
        }
        if(previousBuildingXAxis[y] == 0 || previousSmokeXAxis[y] == 0.0) { // x-1, y
          average += currentValue;
        }
        if(nextBuildingXAxis[y] == 0 || nextSmokeXAxis[y] == 0.0) { // x+1, y
          average += currentValue;
        }

        average = (average + previousSmokeXAxis[y]
            + currentSmokeXAxis[yMinus1] + currentSmokeXAxis[y] + currentSmokeXAxis[yPlus1]
                + nextSmokeXAxis[y]) / 5.0;

        double newValue = (1.0 - fire.getSmokeEvaporationRate()) * average;

        if(newValue > fire.getMaximumSmoke()) {
          newValue = fire.getMaximumSmoke();
        }
        totalSmoke += newValue;
        put[y] = newValue;
        yMinus1 = y;
      }
      previousSmokeXAxis = currentSmokeXAxis;
      currentSmokeXAxis = nextSmokeXAxis;

      previousBuildingXAxis = currentBuildingXAxis;
      currentBuildingXAxis = nextBuildingXAxis;
    }
    burningBuilding.setCurrentSmoke(totalSmoke);
    fire.setCurrentSmokeValue(totalSmoke);
  }


  private void handleFire(FireEntity fire) {
    if(fire.getFireRadius() > 0) {
      burnBuilding(fire);
    }
    else {
      boolean setupFire = setupFire(fire);
      if(setupFire) {
        fire.setFireRadius(1);
      }
      return;
    }
  }

  private void burnBuilding(FireEntity fire) {
    ForestEntity burningBuilding = fire.getBurningForest();
    int minX = burningBuilding.getMinX();
    int maxX = burningBuilding.getMaxX();
    int minY = burningBuilding.getMinY();
    int maxY = burningBuilding.getMaxY();
    double fireSpreadProbability = fire.getFireSpreadProbability();

    double[][] currentFireGridField = fireCanvas.getCurrentFireGrid().field;
    double[][] newFireGridField = fireCanvas.getNewFireGrid().field;
    double[][] currentSmokeField = fireCanvas.getCurrentSmokeGrid().field;
    int[][] buildingField = buildingCanvas.getBuildingsGrid().field;

    for(int x = minX; x < maxX; x++) {
      for(int y = minY; y < maxY; y++) {

        if(buildingField[x][y] == 0 
            || currentFireGridField[x][y] == 0 
            || currentSmokeField[x][y] == 0.0) { // no smoke no fire
          continue;
        }

        formFire(0.0, x, y, fire);
        increaseSmoke(x, y, fire);
        double currentFire = currentFireGridField[x][y];

        if(x > minX) { // left
          if(buildingField[x-1][y] == 1 && random.nextBoolean(fireSpreadProbability)) {
            formFire(currentFire, x-1, y, fire);
            increaseSmoke(x-1, y, fire);
          }
        }

        if(y > minY) { // above
          if(buildingField[x][y-1] == 1 && random.nextBoolean(fireSpreadProbability)) {
            formFire(currentFire, x, y-1, fire);
            increaseSmoke(x, y-1, fire);
          }
        }

        if(x < maxX) { // right
          if(buildingField[x+1][y] == 1 && random.nextBoolean(fireSpreadProbability)) {
            formFire(currentFire, x+1, y, fire);
            increaseSmoke(x+1, y, fire);
          }
        }

        if(y < maxY) { // below
          if(buildingField[x][y+1] == 1 && random.nextBoolean(fireSpreadProbability)) {
            formFire(currentFire, x, y+1, fire);
            increaseSmoke(x, y+1, fire);
          }
        }
      }
    }
    if(storeData) {
      printCsvValues(fire, newFireGridField);
    }

  }

  private void formFire(double currentFire, int x, int y, FireEntity fire){
    double maximumHeat = fire.getMaximumHeat();
    double[][] currentFireField = fireCanvas.getCurrentFireGrid().field;
    double[][] newFireField = fireCanvas.getNewFireGrid().field;
    
    double evaporationRate = fire.getEvaporationRate();
    
    if(currentFire == 0.0) { // middle slot
      currentFire = currentFireField[x][y];
      newFireField[x][y] = newFireValue(currentFire, fire);
    }
    else { // neighboring slots //TODO - Fire never increases
      // I think, we need to the current value to it. currentFireField[x][y]
      // because the neighboring slot only get a partial fire amount from the center slot
      newFireField[x][y] = currentFire * (1 - evaporationRate);
    }
    if(newFireField[x][y] > maximumHeat) {
      newFireField[x][y] = maximumHeat;
    }
    // set the value of the fire
    fire.setCurrentFireValue(fire.getCurrentFireValue()+newFireField[x][y]); 
  }

  private double newFireValue(double currentFire, FireEntity fire) {
    double minDiffProb = fire.getMinimumDiffuseProbability();
    double maxDiffProb = fire.getMaximumDiffuseProbability();
    double minEvapProb = fire.getMinimumEvaporationProbability();
    double maxEvapProb = fire.getMaximumEvaporationProbability();
    boolean increase = random.nextBoolean(fire.getIncrementProbability());
    double newFireValue = Double.MIN_VALUE;
    if(increase) { // exhaust fire
      newFireValue = (1 + getRandom(minDiffProb,maxDiffProb)) * currentFire * (1 - getRandom(minEvapProb,maxEvapProb));
    }
    else { // extinguish fire
      newFireValue = (1 - getRandom(minDiffProb,maxDiffProb)) * currentFire * (1 + getRandom(minEvapProb,maxEvapProb)); 
    }
    return newFireValue;
  }

  private double getRandom(double min, double max) {
    double value = random.nextDouble() * (max - min) + min;
    return value;
  }

  private void increaseSmoke(int x, int y, FireEntity fire) {
    double[][] currentSmokeField = fireCanvas.getCurrentSmokeGrid().field;
    currentSmokeField[x][y] = currentSmokeField[x][y] + fire.getSmokeAmount();
    if(currentSmokeField[x][y] > fire.getMaximumSmoke()) {
      currentSmokeField[x][y] = fire.getMaximumSmoke();
    }
    fire.setCurrentSmokeValue(fire.getCurrentSmokeValue() + currentSmokeField[x][y]);
  }

  private boolean setupFire(FireEntity fire) {
    double[][] newFireGridField = fireCanvas.getNewFireGrid().field;
    double[][] currentSmokeGridField = fireCanvas.getCurrentSmokeGrid().field;
    int x = fire.getX();
    int y = fire.getY();
    double smokeAmount = currentSmokeGridField[x][y];
    if(smokeAmount > fire.getFusableSmokeAmount() && random.nextBoolean(fire.getFusableProbability())) { // orig 0.1
      double initValue = random.nextDouble()
          * (fire.getMaximumHeat() - fire.getMinimumHeat())
          + fire.getMinimumHeat();
      newFireGridField[x][y] = initValue;
      currentSmokeGridField[x][y] = smokeAmount + fire.getSmokeAmount();
      fire.setCurrentSmokeValue(currentSmokeGridField[x][y]);
      if(storeData) {
        printCsvHeaders(fire);
      }
      return true;
    }
    return false;
  }

  private void setupSmoke(FireEntity fire) {
    double[][] newSmokeGridField = fireCanvas.getNewSmokeGrid().field;
    double initValue = random.nextDouble()
        * (fire.getMaximumSmoke() - fire.getMinimumSmoke())
        + fire.getMinimumSmoke();
    newSmokeGridField[fire.getX()][fire.getY()] = initValue;
    fire.setCurrentSmokeValue(initValue);
  }

  private void setupBurningBuilding(FireEntity fire) {
    int buildingId = buildingCanvas.getBuildingId(fire.getX(), fire.getY());
    ForestEntity burningBuilding = buildingPersister.getForest(Long.valueOf(buildingId));
    fire.setBurningForest(burningBuilding);
    burningBuilding.addFire(fire);
  }

  private void printCsvHeaders(FireEntity fire) {
    List<Int2D> coordinates = new ArrayList<>();
    /*Int2D[] minMaxBoundaries = minMaxBuildingCoordinate(fire.getBurningBuilding());
    for(int x = minMaxBoundaries[0].x; x < minMaxBoundaries[1].x; x++) {
      for(int y = minMaxBoundaries[0].y; y < minMaxBoundaries[1].y; y++) {
        coordinates.add(new Int2D(x,y));
      }
    }*/
    coordinates.add(new Int2D(fire.getX(), fire.getY()));
    fireGridCsvPrinter.printHeaders(coordinates);
  }

  private void printCsvValues(FireEntity fire, double[][] newFireGridField) {
    FireGridState fireGridState = new FireGridState();
    fireGridState.setIteration(iteration);
    /*Int2D[] minMaxBoundaries = minMaxBuildingCoordinate(fire.getBurningBuilding());
    for(int x = minMaxBoundaries[0].x; x < minMaxBoundaries[1].x; x++) {
      for(int y = minMaxBoundaries[0].y; y < minMaxBoundaries[1].y; y++) {
        fireGridState.getValues().add(newFireGridField[x][y]);
      }
    }*/
    fireGridState.getValues().add(newFireGridField[fire.getX()][fire.getY()]);
    fireGridCsvPrinter.printRecord(fireGridState);
  }

  public boolean isStoreData() {
    return storeData;
  }

  public void setStoreData(boolean storeData) {
    this.storeData = storeData;
  }
}
