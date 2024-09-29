package ru.sl.jsdranalys.file;

import java.util.Arrays;


public class DSVreader {

	
	public static double[] extactRow(String row, boolean errIgnore) {
		String[] sValues = row.replace(',', '.').split("\\s+"); 
		int counter = 0;
		double [] result = new double[sValues.length];
		for(String s:sValues) {
			if (s.matches("^[-+]?[0-9]*[.,][0-9]+(?:[eE][-+]?[0-9]+)?$")) {
				result[counter++] = Double.parseDouble(s);
			} else if (errIgnore) {
				throw new IllegalArgumentException("Ошибка при разборе строки "+row);
			}
		}
		return result; 
	}
	
	
	public static void main(String[] args) {
		String d = "123.45 35.67.8    45.001";
		
		System.out.println(Arrays.toString(extactRow(d, true)));

	}
	
}
