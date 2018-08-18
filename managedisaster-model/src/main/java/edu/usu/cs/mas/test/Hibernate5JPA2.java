package edu.usu.cs.mas.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.usu.cs.mas.managedisaster.common.CoalitionStatus;
import edu.usu.cs.mas.managedisaster.entity.AgentCoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.ForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionForestEntity;
import edu.usu.cs.mas.managedisaster.entity.CoalitionEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.FireStationEntity;
import edu.usu.cs.mas.managedisaster.entity.TestTableEntity;
import edu.usu.cs.mas.managedisaster.message.Message;
import edu.usu.cs.mas.managedisaster.message.MessageType;
import edu.usu.cs.mas.managedisaster.message.Severity;
import edu.usu.cs.mas.managedisaster.persister.BuildingPersister;
import edu.usu.cs.mas.managedisaster.persister.FirePersister;
import edu.usu.cs.mas.managedisaster.service.ManageDisasterModelSpringConfig;
import sim.engine.Schedule;
import sim.engine.SimState;

public class Hibernate5JPA2 {

  private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";
  private static EntityManagerFactory factory;
  private static EntityManager entityManager;

  private static EntityTransaction transaction;

  public void setup() {
    try {
      factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
      entityManager = factory.createEntityManager();
      transaction = entityManager.getTransaction();
      transaction.begin();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void showVersion() {
    // Check database version
    String sql = "select version()";

    Query versionQuery = entityManager.createNativeQuery(sql);
    String result = (String) versionQuery.getSingleResult();
    System.out.println(result);
  }

  public void save() {
    TestTableEntity entity1 = new TestTableEntity("Entity1");

    entityManager.persist(entity1);

    commit();
  }

  public void find() {
    TestTableEntity entity1 = entityManager.find(TestTableEntity.class, 2L);
    System.out.println(entity1);

    Query query = entityManager.createQuery("select t from TestTableEntity t where t.name = :name");
    query.setParameter("name", "udara");
    entity1 = (TestTableEntity) query.getSingleResult();
    System.out.println(entity1);

    query.setParameter("name", "Entity1");
    @SuppressWarnings("unchecked")
    List<TestTableEntity> entities = query.getResultList();
    for(TestTableEntity entity : entities) {
      System.out.println(entity);
    }
  }
  
  public void findMax() {
    Query query = entityManager.createQuery("select t from TestTableEntity t where t.score = (select max(score) from TestTableEntity where name = 'udara')");
    @SuppressWarnings("unchecked")
    List<TestTableEntity> entities = query.getResultList();
    for(TestTableEntity entity : entities) {
      System.out.println(entity);
    }
  }

  public void max() {
    String strQuery = "select max(t.id) from TestTableEntity t";
    Query query = entityManager.createQuery(strQuery);
    Long nextId = (Long) query.getSingleResult();
    System.out.println("next id = "+nextId);
  }

  public void getBoolean(boolean bool) {
    String strQuery = "select t from TestTableEntity t where t.active = false";
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<TestTableEntity> entities = query.getResultList();
    for(TestTableEntity entity : entities) {
      System.out.println(entity);
    }
  }
  
  public void deleteAll() {
    String strQuery = "delete from TestTableEntity";
    Query query = entityManager.createQuery(strQuery);
    int success = query.executeUpdate();
    System.out.println("Success: "+success);
    commit();
  }

  public void commit() {
    transaction.commit();
    transaction = entityManager.getTransaction();
    transaction.begin();
  }

  public void close() {
    entityManager.close();
    factory.close();
  }
  
  public void saveUtil() {
    TestTableEntity entity1 = new TestTableEntity("Entity1");
    double d = 12345678;
    BigDecimal util = new BigDecimal(d);
    util.setScale(2, RoundingMode.HALF_UP);
    entity1.setUtil(util.doubleValue());
    entityManager.persist(entity1);

    commit();
  }
  
  public void findList() {
    List<Long> ids = Arrays.asList(1L,2L,5L);
    String strQuery = "from TestTableEntity where id in (:ids)";
    Query query = entityManager.createQuery(strQuery);
    query.setParameter("ids", ids);
    @SuppressWarnings("unchecked")
    List<TestTableEntity> entities = query.getResultList();
    for(TestTableEntity entity : entities) {
      System.out.println(entity);
    }
  }
  
  public void findCancelCoal() {
    String strQuery = "select distinct(cb.coalition_id) " + 
      "from managedisaster.coalition_building cb " + 
      "where cb.coalition_id not in " + 
      "(select distinct(coalition_id) " + 
      "from managedisaster.coalition_building " + 
      "where status != 'CANCEL')"; 
    Query query = entityManager.createNativeQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<Integer> coalitionBuildingEntityList = query.getResultList();
    for(Integer coalBuild : coalitionBuildingEntityList) {
      long coalId = (long) coalBuild;
      System.out.println("coalition id: "+coalBuild);
    }
  }
  
  public void findSingleCoalitionBuildings() {
    String strQuery = "select cb.building " + 
        "from CoalitionBuildingEntity cb " + 
        "group by cb.building " + 
        "having count(cb.building) = 1"; 
    Query query = entityManager.createQuery(strQuery);
    @SuppressWarnings("unchecked")
    List<ForestEntity> buildings = query.getResultList();
    for(ForestEntity building : buildings) {
      System.out.println("Buildings: "+building);
    }
  }
  
  public void findUnallocatedCoalition() {
    List<Long> buildIds = Arrays.asList(1l,2l);
    String strQuery = "select cb from CoalitionBuildingEntity cb "
        + "where cb.building.id in (:buildIds)"
        + " and (cb.status is null or (cb.status != 'CANCEL' and cb.status != 'ALLOCATED'))";
      Query query = entityManager.createQuery(strQuery);
      query.setParameter("buildIds", buildIds);
      @SuppressWarnings("unchecked")
      List<CoalitionForestEntity> coalitionBuildingEntityList = query.getResultList();
      for(CoalitionForestEntity coalitionBuildingEntity: coalitionBuildingEntityList) {
        System.out.println(" coal build: "+coalitionBuildingEntity);
      }
        
  }
  
  public void deleteAgentCoal() {
    AgentCoalitionEntity agentCoalitionEntity = entityManager.find(AgentCoalitionEntity.class, 1L);
    entityManager.remove(agentCoalitionEntity);
    commit();
  }
  
  public void findBuilding() {
    String name = "1 MILD FIRE at house4";
    name = name.split("at ")[1];
    String strQuery = "select b from BuildingEntity b where lower(b.name) like '%"+name.toLowerCase()+"%'";
    Query query = entityManager.createQuery(strQuery);
    ForestEntity building = (ForestEntity) query.getSingleResult();
    System.out.println("building is : "+building);
  }

  public static void main(String[] args) {
    Hibernate5JPA2 hibernate5JPA2 = new Hibernate5JPA2();
//    hibernate5JPA2.getFire();
    hibernate5JPA2.setup();
    hibernate5JPA2.findBuilding();
//    hibernate5JPA2.deleteAgentCoal();
    hibernate5JPA2.close();
//    hibernate5JPA2.findUnallocatedCoalition();
//    hibernate5JPA2.findCancelCoal();
//    hibernate5JPA2.findList();
//    hibernate5JPA2.saveUtil();
    /*hibernate5JPA2.showVersion();
  	hibernate5JPA2.save();*/
    //  	hibernate5JPA2.find();
    //  	hibernate5JPA2.max();
//    hibernate5JPA2.getBoolean(false);
    /*for(int i=0; i < 3; i++ ) {
      hibernate5JPA2.save();
    }*/
//    hibernate5JPA2.deleteAll();
//    hibernate5JPA2.findMax();
//    hibernate5JPA2.close();
  }
  
//  @Mock
  private SimState simState;
//  @Mock
  private Schedule schedule;
  
  public void getFire() {
//    MockitoAnnotations.initMocks(this);
    simState.schedule = schedule;
//    Mockito.when(schedule.getTime()).thenReturn(new Double(2));
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ManageDisasterModelSpringConfig.class);
    FirePersister firePersister = applicationContext.getBean(FirePersister.class);
    BuildingPersister buildingPersister = applicationContext.getBean(BuildingPersister.class);
    ForestEntity building = buildingPersister.getBuilding(8L);
    Message message = new Message();
    message.setMessageType(MessageType.FIRE);
    message.setForest(building);
    message.setTweetId(1L);
    for(int i = 0; i < 396; i++) {
      message.setSeverity(Severity.SEVERE);
      firePersister.generateFire(message);
      
      message.setSeverity(Severity.MILD);
      firePersister.generateFire(message);
      
      message.setSeverity(Severity.NORMAL);
      firePersister.generateFire(message);
    }
    
    
  }
}
