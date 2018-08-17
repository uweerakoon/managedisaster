package edu.usu.cs.mas.managedisaster.persister;

import java.util.List;

import sim.engine.SimState;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.message.Message;

public interface FirePersister {
  
  public FireEntity getFire(Long id);

  public List<FireEntity> getAllFires();
  
  public void saveFire(FireEntity fire);
  
  public void setSimState(SimState simState);
  
  public List<FireEntity> getActiveFires();
  
  public List<FireEntity> getUnallcoatedActiveFires();
  
  public List<FireEntity> getCloseFires(double x, double y, long vicinity);
  
  public FireEntity generateFire(Message message);
  
  public void cleanup();
}
