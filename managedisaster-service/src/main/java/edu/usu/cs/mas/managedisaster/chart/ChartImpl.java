package edu.usu.cs.mas.managedisaster.chart;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import edu.usu.cs.mas.managedisaster.service.util.Config;

public class ChartImpl implements Chart {

	private static final Logger LOGGER = Logger.getLogger(ChartImpl.class);
	private static final String PLOT_ENABLE = "edu.usu.cs.mas.managedisaster.plot.enable";
	
	@Inject
	private AgentChart agentChart;
	@Inject
	private BurningForestChart burningForestChart;
	@Inject
	private CoalitionChart coalitionChart;
	@Inject
	private Config config;
	
	@Override
	public void plotCharts() {
		if(!config.getBoolean(PLOT_ENABLE)) {
			return;
		}
		LOGGER.info("Creating agent plots");
		agentChart.createPlots();
		LOGGER.info("Creating Burning Forest plots");
		burningForestChart.createPlots();
		LOGGER.info("Creating Coalition plots");
		coalitionChart.createPlots();
	}
}
