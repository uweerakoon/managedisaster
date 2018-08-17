package edu.usu.cs.mas.managedisaster.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import sim.util.Int2D;


public class FireGridCsvPrinterImpl implements FireGridCsvPrinter{
  
  private static final String NEW_LINE_SEPARATOR = "\n";
  private static final String FOLDER_NAME = "/opt/managedisaster/report/";
  private static final String FILE_NAME = "FireGridStatus.csv";

  private FileWriter fileWriter;
  private CSVPrinter csvFilePrinter;
  private CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
  private List<String> headers;
  
  private List<FireGridState> fireGridStates;
  private boolean printRemaining;
  
  public FireGridCsvPrinterImpl() { }
  
  @Override
  public void init() {
    try {
      deleteFile();
      fileWriter = new FileWriter(FOLDER_NAME + FILE_NAME);
      csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
      headers = new ArrayList<>();
      fireGridStates = new ArrayList<>();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  
  @Override
  public void printRecord(FireGridState fireGridState) {
    if(fireGridState != null){
      fireGridStates.add(fireGridState);
    }
    if(printRemaining || fireGridStates.size() % 1000 == 0) {
      printRecords();
      if(fireGridStates != null) {
      	fireGridStates.clear();
      }
    }
  }
  
  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void printRecords() {
    
    try {
      for(FireGridState fireGridState : fireGridStates) {
        List fireGridStateRecord = new ArrayList<>();
        fireGridStateRecord.add(fireGridState.getIteration());
        for(double value : fireGridState.getValues()) {
          fireGridStateRecord.add(value);
        }
        csvFilePrinter.printRecord(fireGridStateRecord);
      }

    }
    catch(Exception e) {
      System.out.println("Error in CsvFileWriter !!!");
      e.printStackTrace();
    }
  }
  
  @Override
  public void closeFile() {
  	if(fireGridStates == null) {
  		return;
  	}
    try {
      fileWriter.flush();
      fileWriter.close();
      csvFilePrinter.close();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public void printHeaders(List<Int2D> coordinates) {
    headers.add("Index");
    for(Int2D coordinate : coordinates) {
      headers.add(coordinate.toString());
    }
    try {
      csvFilePrinter.printRecord(headers);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void deleteFile() {
    File file = new File(FOLDER_NAME + FILE_NAME);
    if(file.exists()) {
      file.delete();
    }
  }
  
  public boolean isPrintRemaining() {
    return printRemaining;
  }

  @Override
  public void printRemaining() {
  	if(fireGridStates == null) {
  		return;
  	}
    this.printRemaining = true;
    printRecord(null);
  }

}
