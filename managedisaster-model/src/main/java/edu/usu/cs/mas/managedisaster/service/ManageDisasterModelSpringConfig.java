package edu.usu.cs.mas.managedisaster.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinter;
import edu.usu.cs.mas.managedisaster.csv.FireGridCsvPrinterImpl;
//import edu.usu.cs.mas.managedisaster.handler.CommunicationHandler;
//import edu.usu.cs.mas.managedisaster.handler.CommunicationHandlerImpl;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtil;
import edu.usu.cs.mas.managedisaster.model.util.HibernateUtilImpl;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentCoalitionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.AgentPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.AgentStatPersister;
import edu.usu.cs.mas.managedisaster.persister.AgentStatPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.BurningBuildingStatPersister;
import edu.usu.cs.mas.managedisaster.persister.BurningBuildingStatPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionBuildingPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.CoalitionStatPersister;
import edu.usu.cs.mas.managedisaster.persister.CoalitionStatPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.CommunicativePersister;
import edu.usu.cs.mas.managedisaster.persister.CommunicativePersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.FireEnginePersister;
import edu.usu.cs.mas.managedisaster.persister.FireEnginePersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersister;
import edu.usu.cs.mas.managedisaster.persister.FireStationPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.FireTruckPersister;
import edu.usu.cs.mas.managedisaster.persister.FireTruckPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.IntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.IntersectionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.RoadIntersectionPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadIntersectionPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.RoadPersister;
import edu.usu.cs.mas.managedisaster.persister.RoadPersisterImpl;
import edu.usu.cs.mas.managedisaster.persister.TweetPersister;
import edu.usu.cs.mas.managedisaster.persister.TweetPersisterImpl;

@Configuration
public class ManageDisasterModelSpringConfig {

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public HibernateUtil hibernateUtil() {
    return new HibernateUtilImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentPersister agentPersister(){
    return new AgentPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public RoadPersister roadPersister() {
    return new RoadPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CommunicativePersister communicativePersister(){
    return new CommunicativePersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public BuildingPersister buildingPersister(){
    return new BuildingPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public IntersectionPersister intersectionPersister(){
    return new IntersectionPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public RoadIntersectionPersister roadIntersectionPersister(){
    return new RoadIntersectionPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FirePersister firePersister() {
    return new FirePersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public TweetPersister tweetPersister() {
    return new TweetPersisterImpl(); 
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireStationPersister fireStationPersister() {
    return new FireStationPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionPersister coalitionPersister() {
    return new CoalitionPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireEnginePersister fireEnginePersister() {
    return new FireEnginePersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireTruckPersister fireTruckPersister() {
    return new FireTruckPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentStatPersister agentStatPersister() {
    return new AgentStatPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public BurningBuildingStatPersister burningBuildingStatPersister() {
    return new BurningBuildingStatPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionStatPersister coalitionStatPersister() {
    return new CoalitionStatPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CoalitionBuildingPersister coalitionBuildingPersister() {
    return new CoalitionBuildingPersisterImpl();
  }

  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public FireGridCsvPrinter fireGridCsvPrinter() {
    return new FireGridCsvPrinterImpl();
  }
  
  @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public AgentCoalitionPersister agentCoalitionPersister() {
    return new AgentCoalitionPersisterImpl();
  }

  /* @Bean
  @Scope(BeanDefinition.SCOPE_SINGLETON)
  public CommunicationHandler communicationHandler(){
    return new CommunicationHandlerImpl(agentSociety(), 
      communicationNetwork(), communicativePersister());
  }*/
}
