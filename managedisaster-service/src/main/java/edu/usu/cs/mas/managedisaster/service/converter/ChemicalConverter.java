package edu.usu.cs.mas.managedisaster.service.converter;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerConverter;

import edu.usu.cs.mas.managedisaster.common.Chemical;

public class ChemicalConverter extends DozerConverter<Chemical, String>{
  
  public ChemicalConverter() {
    super(Chemical.class, String.class);
  }
  
  @Override
  public String convertTo(Chemical chemical, String strChemical) {
    return chemical != null
        ? chemical.toString()
        : null;
  }
  
  @Override
  public Chemical convertFrom(String strChemical, Chemical chemical) {
    return StringUtils.isNotEmpty(strChemical)
        ? Chemical.valueOf(strChemical)
        : null;
  }

}
