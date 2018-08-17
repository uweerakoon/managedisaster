package edu.usu.cs.mas.managedisaster.csv;

import java.util.List;

import sim.util.Int2D;

public interface FireGridCsvPrinter {

  public void printRecords();
  
  public void closeFile();
  
  public void printHeaders(List<Int2D> coordinates);
  
  public void printRemaining();
  
  public void printRecord(FireGridState fireGridState);
  
  public void deleteFile();
  
  public void init();
}
