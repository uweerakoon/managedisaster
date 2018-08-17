package edu.usu.cs.mas.managedisaster.temporary;

import java.awt.Color;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String str = "1 mild fire in walmart 2";
        System.out.println(str.matches(".*\\d.*"));
        
        String numberOnly= str.replaceAll("[^0-9]", "");
        System.out.println(numberOnly);
        
        System.out.println(new Scanner(str).useDelimiter("[^\\d]+").nextInt());
    }
}
