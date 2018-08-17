package edu.usu.cs.mas.managedisaster.chart;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.io.CSV;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class FireGridLineChart extends ApplicationFrame{

  private static final long serialVersionUID = 125212321720471508L;
  private static final String FOLDER_NAME = "/opt/managedisaster/report/";
  private static final String INPUT_FILE_NAME = "FireGridStatus.csv";
  private String imageFileName = "FireGridStatus.png";

  public FireGridLineChart(String applicationTitle) {
    super(applicationTitle);
  }
  
  public JFreeChart createGraph(String chartTitle) {
    JFreeChart lineChart = ChartFactory.createXYLineChart(
      chartTitle,
      "Hotspot value","Iteration",
//      createCsvDataset(),
      createXYDataSet(),
      PlotOrientation.HORIZONTAL,
      true,true,false);
      
    custermizeAppearence(lineChart);
    
    savePictureFile(lineChart);
    return lineChart;
  }
  
  public void setChartPanel(JFreeChart lineChart) {
    ChartPanel chartPanel = new ChartPanel( lineChart );
//  chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
   setContentPane( chartPanel );
  }
  
  private void savePictureFile(JFreeChart lineChart) {
    BufferedImage image = lineChart.createBufferedImage(560, 367);
    
    try {
      File imageFile = new File(FOLDER_NAME + imageFileName);
      ImageIO.write(image, "png", imageFile);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void custermizeAppearence(JFreeChart lineChart) {
    lineChart.setBackgroundPaint(Color.white);
    final XYPlot plot = lineChart.getXYPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesLinesVisible(0, false);
    renderer.setSeriesShapesVisible(1, false);
    plot.setRenderer(renderer);
    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
  }

  // TODO - it is not easy to create csv dataset from the jfree csv reader
  @SuppressWarnings("unused")
  private CategoryDataset createCsvDataset( ) {
    CategoryDataset categoryDataset = null;
    CSV csvReader = new CSV();
    Reader csvFileReader = null; 
    try {
      csvFileReader = new FileReader(FOLDER_NAME + INPUT_FILE_NAME);
      categoryDataset = csvReader.readCategoryDataset(csvFileReader);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return categoryDataset;
  }
  
  private XYDataset createXYDataSet() {
    XYSeries xyseries = new XYSeries("Hotspot data plot");
    FileReader fileReader = null;
    CSVParser csvParser = null;
    CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(new String[] {"Index", "Int2D[70,35]"});
    try {
      fileReader = new FileReader(FOLDER_NAME + INPUT_FILE_NAME);
      csvParser = new CSVParser(fileReader, csvFormat);
      List<CSVRecord> csvRecords = csvParser.getRecords();
      for(int i=1; i < csvRecords.size(); i++) {
        CSVRecord csvRecord = csvRecords.get(i);
        double x = Double.parseDouble(csvRecord.get(0));
        double y = Double.parseDouble(csvRecord.get(1));
        xyseries.add(y, x);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    finally {
      try {
        fileReader.close();
        csvParser.close();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
    
    final XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
    xySeriesCollection.addSeries(xyseries);
    return xySeriesCollection; 
  }
  
  public String getImageFileName() {
    return imageFileName;
  }

  public void setImageFileName(String imageFileName) {
    this.imageFileName = imageFileName;
  }

  public static void main(String[] args) {
    FireGridLineChart fireGridLineChart = new FireGridLineChart(
      "Iteration Vs Fire");
    JFreeChart lineChart = fireGridLineChart.createGraph("Iteration Index Vs Fire Amount");
    fireGridLineChart.setChartPanel(lineChart);
    fireGridLineChart.pack();
    RefineryUtilities.centerFrameOnScreen(fireGridLineChart);
    fireGridLineChart.setVisible(true);
  }

}
