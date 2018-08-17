package edu.usu.cs.mas.managedisaster.service.converter;

import java.awt.Color;

import org.dozer.DozerConverter;

@SuppressWarnings("unused")
public class ColorToIntegerConverter extends DozerConverter<Color, Integer>{
  
  public ColorToIntegerConverter(){
    super(Color.class, Integer.class);
  }
  
  @Override
  public Integer convertTo(Color color, Integer rgbValue){
    return color != null
        ? color.getRGB()
        : null;
  }

  @Override
  public Color convertFrom(Integer rgbValue, Color color){
    return rgbValue != null
        ? new Color(rgbValue)
    : null;
  }
}
