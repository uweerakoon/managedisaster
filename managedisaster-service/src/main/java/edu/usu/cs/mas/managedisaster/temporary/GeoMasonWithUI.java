package edu.usu.cs.mas.managedisaster.temporary;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.vividsolutions.jts.io.ParseException;

import campusworld.CampusWorld;
import campusworld.CampusWorldWithUI;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.geo.GeomVectorFieldPortrayal;

public class GeoMasonWithUI extends GUIState {
  
  private Display2D display;
  private JFrame displayFrame;
  
  private GeomVectorFieldPortrayal geomVectorFieldPortrayal = new GeomVectorFieldPortrayal();
  
  public GeoMasonWithUI(SimState state) {
    super(state);
  }
  
  public GeoMasonWithUI() throws ParseException {
    super(new GeoMason(System.currentTimeMillis()));
  }
  
  @Override
  public void init(Controller controller) {
      super.init(controller);

      display = new Display2D(GeoMason.WIDTH, GeoMason.HEIGHT, this);
      display.attach(geomVectorFieldPortrayal, "Utah", true);
      
      displayFrame = display.createFrame();
      controller.registerFrame(displayFrame);
      displayFrame.setVisible(true);
  }
  
  @Override
  public void start() {
      super.start();
      setupPortrayals();
  }
  
  private void setupPortrayals() {
    GeoMason geoMason = (GeoMason) state;
    geomVectorFieldPortrayal.setField(geoMason.vectorField);
    // Need to set the portrayal for all
    display.reset();
    display.setBackdrop(Color.WHITE);

    display.repaint();
  }
  
  public static void main(String[] args) {
      GeoMasonWithUI geoMasonWithUI = null;

      try
      {
        geoMasonWithUI = new GeoMasonWithUI();
      }
      catch (ParseException ex)
      {
          Logger.getLogger(CampusWorldWithUI.class.getName()).log(Level.SEVERE, null, ex);
      }

      Console console = new Console(geoMasonWithUI);
      console.setVisible(true);
  }

}
