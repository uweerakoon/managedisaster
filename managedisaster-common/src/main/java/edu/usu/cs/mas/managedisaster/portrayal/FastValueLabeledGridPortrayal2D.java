package edu.usu.cs.mas.managedisaster.portrayal;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import sim.field.grid.Grid2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.util.Int2D;

public class FastValueLabeledGridPortrayal2D extends FastValueGridPortrayal2D {
	private static final int OFFSET_X = 5;
	private static final int OFFSET_Y = 12;
	private Map<Int2D, String> positionLabelMap = new HashMap<>();
	
	public FastValueLabeledGridPortrayal2D(String name) {
		super(name);
	}
	
	public FastValueLabeledGridPortrayal2D(String valueName, boolean immutableField) {
		super(valueName, immutableField);
	}
	
	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		super.draw(object, graphics, info);
		final Grid2D field = (Grid2D)this.field;
		if(field == null) {
			return;
		}
		if(positionLabelMap.isEmpty()) {
			return;
		}
		final int maxX = field.getWidth();
    final int maxY = field.getHeight();
    final double xScale = info.draw.width / maxX;
    final double yScale = info.draw.height / maxY;
		for(Map.Entry<Int2D, String> entry : positionLabelMap.entrySet()) {
			int x = (int)(entry.getKey().x * xScale + OFFSET_X);
			int y = (int)(entry.getKey().y * yScale + OFFSET_Y);
			String s = entry.getValue();
			graphics.drawString(s,x,y);
		}
		
	}
	
	public void addLabel(Int2D position, String label) {
		positionLabelMap.put(position, label);
	}
 
}
