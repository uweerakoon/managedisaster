package edu.usu.cs.mas.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatter {
  public static void main(String[] args) {
    double d = 345678923.3245677;
    String pattern = "#.##";
    DecimalFormat myFormatter = new DecimalFormat(pattern);
    String fd = myFormatter.format(d);
    double newval = Double.valueOf(fd);
//    myFormatter.setRoundingMode(RoundingMode.UP);
//    myFormatter.format(d);
//    Math.round(d,2);
//    BigDecimal bd = new BigDecimal(d);
//    bd = bd.setScale(2, RoundingMode.HALF_UP);
//    double newval = bd.doubleValue();
    System.out.println("old value: "+d+"   new value: "+newval+" String: "+fd);
    
    d = 12345678.12545;
    BigDecimal util = new BigDecimal(d);
    util.setScale(2, RoundingMode.HALF_UP);
    d= util.doubleValue();
    System.out.println("new value: "+d);
    
  }
}
