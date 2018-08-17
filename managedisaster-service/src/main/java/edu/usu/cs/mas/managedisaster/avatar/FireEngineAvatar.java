package edu.usu.cs.mas.managedisaster.avatar;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.usu.cs.mas.managedisaster.player.FireEnginePlayer;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class FireEngineAvatar extends RectanglePortrayal2D {

	private static final long serialVersionUID = 7406693100664813611L;
	
	private FireEnginePlayer fireEnginePlayer;
	
	public FireEngineAvatar(FireEnginePlayer fireEnginePlayer) {
		this.fireEnginePlayer = fireEnginePlayer;
	}

	@Override
  public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
//		FireEngineAvatar fireEngineAvatar = (FireEngineAvatar) object;
		paint = Color.red;
		super.draw(object, graphics, info);
	}

	public FireEnginePlayer getFireEnginePlayer() {
		return fireEnginePlayer;
	}
}
