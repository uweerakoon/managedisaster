package edu.usu.cs.mas.managedisaster.player;

import edu.usu.cs.mas.managedisaster.avatar.FireTruckAvatar;
import edu.usu.cs.mas.managedisaster.model.FireTruckModel;
import sim.engine.SimState;
import sim.engine.Steppable;

public class FireTruckPlayer implements Steppable {
	
	private static final long serialVersionUID = -5543376339448214654L;
	
	private FireTruckModel fireTruckModel;
	private FireTruckAvatar fireTruckAvatar;
	
	public FireTruckPlayer(FireTruckModel fireTruckModel) {
		this.fireTruckModel = fireTruckModel;
		fireTruckAvatar = new FireTruckAvatar(this);
	}
	
	@Override
	public void step(SimState state) {
		
	}
	
	public Long getId() {
		return fireTruckModel.getId();
	}
	
	public FireTruckAvatar getFireTruckAvatar() {
		return fireTruckAvatar;
	}

	public void setFireTruckAvatar(FireTruckAvatar fireTruckAvatar) {
		this.fireTruckAvatar = fireTruckAvatar;
	}

	public Integer getX() {
		return fireTruckModel.getX();
	}
	public void setX(Integer x) {
		fireTruckModel.setX(x);
	}
	public Integer getY() {
		return fireTruckModel.getY();
	}
	public void setY(Integer y) {
		fireTruckModel.setY(y);
	}

}
