package edu.usu.cs.mas.managedisaster.temporary;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import sim.engine.SimState;
import sim.field.geo.GeomVectorField;
import sim.io.geo.ShapeFileImporter;

public class GeoMason extends SimState {
  
  private static final long serialVersionUID = 348213939569696485L;
  
  // WIDTH and HEIGHT correspond to arbitrary display dimensions
  public final static int WIDTH = 300;
  public final static int HEIGHT = 300;
  /** Fields to hold the associated GIS information */
  GeomVectorField vectorField = new GeomVectorField(WIDTH, HEIGHT);
  
  public GeoMason(long seed) {
    super(seed);
    loadVectorField();
  }
  
  public void loadVectorField() {
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      String fileName = classLoader.getResource("Utah/Utah.shp").getFile();
      URL utahShapeFileUrl = new File(fileName).toURI().toURL(); 
      ShapeFileImporter.read(utahShapeFileUrl, vectorField);
    }
    catch(FileNotFoundException fex) {
      fex.printStackTrace();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    
  }
}
