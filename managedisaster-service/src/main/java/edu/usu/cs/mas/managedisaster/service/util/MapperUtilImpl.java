package edu.usu.cs.mas.managedisaster.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEngineEntity;
import edu.usu.cs.mas.managedisaster.entity.FireTruckEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.model.FireEngineModel;
import edu.usu.cs.mas.managedisaster.model.FireTruckModel;

public class MapperUtilImpl implements MapperUtil{
  
  private static final String MAPPING_FILE_NAME = "dozer_mappings.xml";
  
  private final Mapper mapper = new DozerBeanMapper(Arrays.asList(MAPPING_FILE_NAME));
  
  @Override
  public List<AgentModel> getAgentsFromAgentEntityList(List<AgentEntity> listAgentEntity){
    List<AgentModel> listAgents = new ArrayList<>();
    
    for(AgentEntity agentEntity : listAgentEntity){
      listAgents.add(mapper.map(agentEntity, AgentModel.class));
    }
    
    return listAgents;
  }
  
  @Override
  public List<FireEngineModel> getFireEngineModels(List<FireEngineEntity> entities) {
  	List<FireEngineModel> models = new ArrayList<>();
  	for(FireEngineEntity entity : entities) {
  		models.add(mapper.map(entity, FireEngineModel.class));
  	}
  	return models;
  }
  
  @Override
  public List<FireTruckModel> getFireTruckModels(List<FireTruckEntity> entities) {
  	List<FireTruckModel> models = new ArrayList<>();
  	for(FireTruckEntity entity : entities) {
  		models.add(mapper.map(entity, FireTruckModel.class));
  	}
  	return models;
  }

}
