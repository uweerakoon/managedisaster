package edu.usu.cs.mas.managedisaster.csv;

import java.util.ArrayList;
import java.util.List;


public class FireGridState {
  
  private int iteration;
  private List<Double> values;
  
  public int getIteration() {
    return iteration;
  }
  public void setIteration(int iteration) {
    this.iteration = iteration;
  }
  public List<Double> getValues() {
    if(values == null) {
      values = new ArrayList<Double>();
    }
    return values;
  }
  public void setValues(List<Double> values) {
    this.values = values;
  }
}
