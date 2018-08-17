package edu.usu.cs.mas.managedisaster.service.util;

import java.util.List;

import edu.usu.cs.mas.managedisaster.entity.AgentEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEngineEntity;
import edu.usu.cs.mas.managedisaster.entity.FireTruckEntity;
import edu.usu.cs.mas.managedisaster.model.AgentModel;
import edu.usu.cs.mas.managedisaster.model.FireEngineModel;
import edu.usu.cs.mas.managedisaster.model.FireTruckModel;

public interface MapperUtil {
  /**
   * Maps data base agent records to agent objects
   * @param listAgentEntity
   * @return list of agents
   */
  public List<AgentModel> getAgentsFromAgentEntityList(List<AgentEntity> listAgentEntity);
  
  public List<FireEngineModel> getFireEngineModels(List<FireEngineEntity> entities);
  
  public List<FireTruckModel> getFireTruckModels(List<FireTruckEntity> entities);

}
