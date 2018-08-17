package edu.usu.cs.mas.managedisaster.avatar;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.usu.cs.mas.managedisaster.player.FireTruckPlayer;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class FireTruckAvatar extends RectanglePortrayal2D {

	private static final long serialVersionUID = -6320467496536548262L;
	
	private FireTruckPlayer fireTruckPlayer;
	
	public FireTruckAvatar(FireTruckPlayer fireTruckPlayer) {
		this.fireTruckPlayer = fireTruckPlayer;
	}
	
	@Override
  public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		paint = Color.orange;
		super.draw(object, graphics, info);
	}

	public FireTruckPlayer getFireTruckPlayer() {
		return fireTruckPlayer;
	}
}
