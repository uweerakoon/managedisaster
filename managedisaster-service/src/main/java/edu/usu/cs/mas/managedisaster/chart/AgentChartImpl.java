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
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentStatEntity;
import edu.usu.cs.mas.managedisaster.persister.AgentStatPersister;

public class AgentChartImpl extends JFrame implements AgentChart {

	private static final long serialVersionUID = -3345673479227944405L;
	private static final MersenneTwisterFast RANDOM = new MersenneTwisterFast(100);
	private static final String FOLDER_NAME = "/opt/managedisaster/report/";
	private static final String AGENT_STATUS_PLOT = "agentstatusplot_"+new Date()+".png";
	
	private static final Font TITLE_FONT = new Font("Serif", java.awt.Font.BOLD, 18);
	private static final String PLOT_HEADER = "Agent Status Vs Time";
	
	private static final String X_AXIS_NAME = "Time";
	private static final String Y_AXIS_NAME = "Agent Status";
	private static final PlotOrientation ORIENTATION = PlotOrientation.VERTICAL;
	private static final boolean IS_LEGENDS = true;
	private static final boolean IS_TOOLTIP = true;
	private static final boolean IS_URL = false;
	
	@Inject
	private AgentStatPersister agentStatPersister;
	
	//http://zetcode.com/java/jfreechart/
	private JFreeChart linePlot;
	private Map<Long/*agent id*/, XYSeries /*plot series*/> plotMap = new HashMap<>();
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
		List<AgentStatEntity> agentStats = agentStatPersister.getAllAgentStats();
		if(CollectionUtils.isEmpty(agentStats)) {
			return false;
		}
		for(AgentStatEntity agentStat : agentStats) {
			long agentId = agentStat.getAgent().getId();
			AgentStatus status = agentStat.getStatus();
			XYSeries agentPlot = null;
			if(plotMap.containsKey(agentId)) {
				agentPlot = plotMap.get(agentId);
			}
			else {
				agentPlot = new XYSeries("Agent Status. ID: "+agentId);
				plotMap.put(agentId, agentPlot);
			}
			agentPlot.add(agentStat.getTime(), status.getStatusId());
		}
		return true;
	}
	
	private void addAllPlotsToCollection() {
		for(XYSeries plot : plotMap.values()) {
			dataset.addSeries(plot);
		}
	}
	
	
	private void createChart() {
		linePlot = ChartFactory.createXYLineChart(PLOT_HEADER, X_AXIS_NAME, Y_AXIS_NAME, 
					        dataset, ORIENTATION, IS_LEGENDS, IS_TOOLTIP, IS_URL);
		XYPlot plot = linePlot.getXYPlot();
		
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		for(int i=0; i < plotMap.size(); i++) {
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
    	plotFile = new File(FOLDER_NAME+AGENT_STATUS_PLOT);
    	ChartUtilities.saveChartAsPNG(plotFile, linePlot, Chart.WIDTH, Chart.HEIGHT);
    }
    catch(Exception e) {
    	e.printStackTrace();
    }
	}
}
