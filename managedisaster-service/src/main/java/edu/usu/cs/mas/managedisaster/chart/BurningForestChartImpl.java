package edu.usu.cs.mas.managedisaster.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.JFrame;

import org.apache.commons.collections.CollectionUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ec.util.MersenneTwisterFast;
import edu.usu.cs.mas.managedisaster.entity.BurningForestStatEntity;
import edu.usu.cs.mas.managedisaster.persister.BurningForestStatPersister;

public class BurningForestChartImpl extends JFrame implements BurningForestChart {

	private static final long serialVersionUID = -3345673479227944405L;
	private static final MersenneTwisterFast RANDOM = new MersenneTwisterFast(100);
	private static final String FOLDER_NAME = "/opt/managedisaster/report/";
	private static final String BURNING_FOREST_STATUS_PLOT = "burningforeststatusplot_"+new Date()+".png";
	
	private static final Font TITLE_FONT = new Font("Serif", java.awt.Font.BOLD, 18);
	private static final String PLOT_HEADER = "Burning Forest Vs Time";
	
	private static final String X_AXIS_NAME = "Time";
	private static final String Y_AXIS_NAME = "Burning Forest Status";
	private static final PlotOrientation ORIENTATION = PlotOrientation.VERTICAL;
	private static final boolean IS_LEGENDS = true;
	private static final boolean IS_TOOLTIP = true;
	private static final boolean IS_URL = false;
	
	@Inject
	private BurningForestStatPersister burningForestStatPersister;
	
	private JFreeChart linePlot;
	private Map<Long, XYSeries> firePlotMap = new HashMap<>();
	private Map<Long, XYSeries> smokePlotMap = new HashMap<>();
	private Map<Long, XYSeries> waterPlotMap = new HashMap<>();
	private XYSeriesCollection dataset = new XYSeriesCollection();
	private File plotFile;
	
	@Override
	public void createPlots() {
		boolean hasPlots = addXYSeriesData();
		if(!hasPlots) {
			return;
		}
		addAllPlotsToCollection();
		createChart();
	}
	
	private boolean addXYSeriesData() {
		List<BurningForestStatEntity> forestStats = burningForestStatPersister.getAllBurningForestStats();
		if(CollectionUtils.isEmpty(forestStats)) {
			return false;
		}
		for(BurningForestStatEntity forestStat : forestStats) {
			long forestId = forestStat.getForest().getId();
			XYSeries firePlot = null, smokePlot = null, waterPlot = null;
			if(firePlotMap.containsKey(forestId)) {
				firePlot = firePlotMap.get(forestId);
				smokePlot = smokePlotMap.get(forestId);
				waterPlot = waterPlotMap.get(forestId);
			}
			else {
				firePlot = new XYSeries("Forest Fire. ID: "+forestId);
				firePlotMap.put(forestId, firePlot);
				smokePlot = new XYSeries("Forest Smoke. ID: "+forestId);
				smokePlotMap.put(forestId, smokePlot);
				waterPlot = new XYSeries("Forest Water. ID: "+forestId);
				waterPlotMap.put(forestId, waterPlot);
			}
			firePlot.add(forestStat.getTime(), forestStat.getFireAmount());
			smokePlot.add(forestStat.getTime(), forestStat.getSmokeAmount());
			waterPlot.add(forestStat.getTime(), forestStat.getWaterAmount());
		}
		return true;
	}
	
	private void addAllPlotsToCollection() {
		for(XYSeries plot : firePlotMap.values()) {
			dataset.addSeries(plot);
		}
		for(XYSeries plot : smokePlotMap.values()) {
			dataset.addSeries(plot);
		}
		for(XYSeries plot : waterPlotMap.values()) {
			dataset.addSeries(plot);
		}
	}
	
	
	private void createChart() {
		linePlot = ChartFactory.createXYLineChart(PLOT_HEADER, X_AXIS_NAME, Y_AXIS_NAME, 
					        dataset, ORIENTATION, IS_LEGENDS, IS_TOOLTIP, IS_URL);
		XYPlot plot = linePlot.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		int numberOfPlots = firePlotMap.size() + waterPlotMap.size() + waterPlotMap.size();
		for(int i=0; i < numberOfPlots; i++) {
			float r = RANDOM.nextFloat();
			float g = RANDOM.nextFloat();
			float b = RANDOM.nextFloat();
			Color randomColor = new Color(r, g, b);
	    renderer.setSeriesPaint(i, randomColor.darker());
	    renderer.setSeriesStroke(i, new BasicStroke(2.0f));
		}
    
    plot.setRenderer(renderer);
    plot.setBackgroundPaint(Color.white);

    plot.setRangeGridlinesVisible(true);
    plot.setRangeGridlinePaint(Color.BLACK);

    plot.setDomainGridlinesVisible(true);
    plot.setDomainGridlinePaint(Color.BLACK);

    linePlot.getLegend().setFrame(BlockBorder.NONE);

    linePlot.setTitle(new TextTitle(PLOT_HEADER, TITLE_FONT ));
    
    try {
    	plotFile = new File(FOLDER_NAME+BURNING_FOREST_STATUS_PLOT);
    	ChartUtilities.saveChartAsPNG(plotFile, linePlot, Chart.WIDTH, Chart.HEIGHT);
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
	}
}