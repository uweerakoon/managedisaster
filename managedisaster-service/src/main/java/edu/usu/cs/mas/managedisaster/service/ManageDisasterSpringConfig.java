package edu.usu.cs.mas.managedisaster.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.usu.cs.mas.managedisaster.canvas.ForestCanvas;
import edu.usu.cs.mas.managedisaster.canvas.ForestCanvasImpl;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireCanvasImpl;
import edu.usu.cs.mas.managedisaster.canvas.FireStationCanvas;
import edu.usu.cs.mas.managedisaster.canvas.FireStationCanvasImpl;
import edu.usu.cs.mas.managedisaster.canvas.PositioningCanvas;
import edu.usu.cs.mas.managedisaster.canvas.PositioningCanvasImpl;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvas;
import edu.usu.cs.mas.managedisaster.canvas.RoadCanvasImpl;
import edu.usu.cs.mas.managedisaster.chart.AgentChart;
import edu.usu.cs.mas.managedisaster.chart.AgentChartImpl;
import edu.usu.cs.mas.managedisaster.chart.BurningForestChart;
import edu.usu.cs.mas.managedisaster.chart.BurningForestChartImpl;
import edu.usu.cs.mas.managedisaster.chart.Chart;
import edu.usu.cs.mas.managedisaster.chart.ChartImpl;
import edu.usu.cs.mas.managedisaster.chart.CoalitionChart;
import edu.usu.cs.mas.managedisaster.chart.CoalitionChartImpl;
import edu.usu.cs.mas.managedisaster.collection.AgentSociety;
import edu.usu.cs.mas.managedisaster.collection.AgentSocietyImpl;
import edu.usu.cs.mas.managedisaster.collection.FireEngineCollection;
import edu.usu.cs.mas.managedisaster.collection.FireEngineCollectionImpl;
import edu.usu.cs.mas.managedisaster.collection.FireTruckCollection;
import edu.usu.cs.mas.managedisaster.collection.FireTruckCollectionImpl;
import edu.usu.cs.mas.managedisaster.display.TransportationLayout;
import edu.usu.cs.mas.managedisaster.display.TransportationLayoutImpl;
import edu.usu.cs.mas.managedisaster.handler.ChemicalRefiller;
import edu.usu.cs.mas.managedisaster.handler.CoalitionCalculator;
import edu.usu.cs.mas.managedisaster.handler.CoalitionCalculatorImpl;
import edu.usu.cs.mas.managedisaster.handler.CoalitionCleaner;
import edu.usu.cs.mas.managedisaster.handler.CoalitionCleanerImpl;
import edu.usu.cs.mas.managedisaster.handler.CoalitionFormation;
import edu.usu.cs.mas.managedisaster.handler.CoalitionFormationImpl;
import edu.usu.cs.mas.managedisaster.handler.CoalitionOptimizer;
import edu.usu.cs.mas.managedisaster.handler.CoalitionOptimizerImpl;
import edu.usu.cs.mas.managedisaster.handler.CommunicationHandler;
import edu.usu.cs.mas.managedisaster.handler.CommunicationHandlerImpl;
import edu.usu.cs.mas.managedisaster.handler.DataCuller;
import edu.usu.cs.mas.managedisaster.handler.Extinguisher;
import edu.usu.cs.mas.managedisaster.handler.ExtinguisherImpl;
import edu.usu.cs.mas.managedisaster.handler.FireDiffuser;
import edu.usu.cs.mas.managedisaster.handler.MovementHandler;
import edu.usu.cs.mas.managedisaster.handler.MovementHandlerImpl;
import edu.usu.cs.mas.managedisaster.handler.TweetDispatcher;
import edu.usu.cs.mas.managedisaster.handler.TwitterHandler;
import edu.usu.cs.mas.managedisaster.handler.TwitterHandlerImpl;
import edu.usu.cs.mas.managedisaster.network.CommunicationNetwork;
import edu.usu.cs.mas.managedisaster.network.CommunicationNetworkImpl;
import edu.usu.cs.mas.managedisaster.persister.AgentUtilityPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentUtilityPersisterImpl;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlanner;
import edu.usu.cs.mas.managedisaster.service.planner.RoutePlannerImpl;
import edu.usu.cs.mas.managedisaster.service.util.Cleaner;
import edu.usu.cs.mas.managedisaster.service.util.CleanerImpl;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import edu.usu.cs.mas.managedisaster.service.util.ConfigImpl;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtil;
import edu.usu.cs.mas.managedisaster.service.util.MapperUtilImpl;

@Configuration
public class ManageDisasterSpringConfig {

  private static final Logger LOGGER = Logger.getLogger(ManageDisasterSpringConfig.class);

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public PositioningCanvas positioningCanvas(){
    return new PositioningCanvasImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public ForestCanvas buildingCanvas(){
    return new ForestCanvasImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public RoadCanvas roadCanvas(){
    return new RoadCanvasImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireCanvas fireCanvas(){
    return new FireCanvasImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireStationCanvas fireStationCanvas() {
    return new FireStationCanvasImpl();
  }
  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public MapperUtil mapperUtil() {
    return new MapperUtilImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public TransportationLayout transportationLayout(){
    return new TransportationLayoutImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public MovementHandler movementHandler(){
    MovementHandlerImpl movementHandlerImpl = new MovementHandlerImpl();
    return movementHandlerImpl;
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public RoutePlanner routePlanner(){
    return new RoutePlannerImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentSociety agentSociety(){
    return new AgentSocietyImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireEngineCollection fireEngineCollection() {
    return new FireEngineCollectionImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireTruckCollection fireTruckCollection() {
    return new FireTruckCollectionImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CommunicationNetwork communicationNetwork(){
    return new CommunicationNetworkImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CommunicationHandler communicationHandler(){
    return new CommunicationHandlerImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireDiffuser fireDiffuser() {
    return new FireDiffuser();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public ChemicalRefiller chemicalRefiller() {
    return new ChemicalRefiller();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public Extinguisher fireExtinguisher() {
    return new ExtinguisherImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public TwitterHandler twitterHandler() {
    return new TwitterHandlerImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public Cleaner cleaner() {
    return new CleanerImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public TweetDispatcher tweetDispatcher() {
    return new TweetDispatcher();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public Config config() {
    return new ConfigImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public DataCuller dataCuller() {
    return new DataCuller();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public Chart chart() {
    return new ChartImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentChart agentChart() {
    return new AgentChartImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public BurningForestChart burningBuildingChart() {
    return new BurningForestChartImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionChart coalitionChart() {
    return new CoalitionChartImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionFormation coalitionFormation() {
    return new CoalitionFormationImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionOptimizer coalitionOptimizer() {
    return new CoalitionOptimizerImpl();
  }
  
  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionCleaner coalitionCleaner() {
    return new CoalitionCleanerImpl();
  }
  
  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionCalculator coalitionCalculator() {
    return new CoalitionCalculatorImpl();
  }
  
  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentUtilityPersister agentUtilityPersister() { 
    return new AgentUtilityPersisterImpl();
  }
}
