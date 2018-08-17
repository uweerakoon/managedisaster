package edu.usu.cs.mas.managedisaster.player;

import edu.usu.cs.mas.managedisaster.avatar.FireEngineAvatar;
import edu.usu.cs.mas.managedisaster.model.FireEngineModel;
import sim.engine.SimState;
import sim.engine.Steppable;

public class FireEnginePlayer implements Steppable {
	
	private static final long serialVersionUID = 6565771648372697325L;
	
	private FireEngineModel fireEngineModel;
	private FireEngineAvatar fireEngineAvatar;
	
	public FireEnginePlayer(FireEngineModel fireEngineModel) {
		this.fireEngineModel = fireEngineModel;
		fireEngineAvatar = new FireEngineAvatar(this);
	}
	
	@Override
	public void step(SimState state) {
		
	}
	
	public Long getId() {
		return fireEngineModel.getId();
	}
	
	public FireEngineAvatar getFireEngineAvatar() {
		return fireEngineAvatar;
	}

	public void setFireEngineAvatar(FireEngineAvatar fireEngineAvatar) {
		this.fireEngineAvatar = fireEngineAvatar;
	}

	public Integer getX() {
		return fireEngineModel.getX();
	}
	public void setX(Integer x) {
		fireEngineModel.setX(x);
	}
	
	public Integer getY() {
		return fireEngineModel.getY();
	}
	public void setY(Integer y) {
		fireEngineModel.setY(y);
	}
}
