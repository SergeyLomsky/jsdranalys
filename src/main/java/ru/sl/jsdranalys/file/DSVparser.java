package ru.sl.jsdranalys.file;

import java.util.Arrays;

/**
 * Класс перевода массива данных хранящихся в формате DSV
 * в массив double[][]
 * 
 * @author SergeyLomsky
 */
public class DSVparser {
	
	private String[] strArray;
	
	boolean errIgnore = false;
	
	private int numCol = 2;
	
	public DSVparser(String inData) {
		this.strArray = inData.split("\n");	
	}

	/**
	 * Функция разбора всего текстового массива
	 * @return выходной массив
	 * @throws IllegalArgumentException
	 */
	public double[][] parse() throws IllegalArgumentException{
		int len = strArray.length;
		double [][] result = new double [len][numCol];
		for (int i = 0; i < len; i++) {
			double [] row = extactRow(strArray[i], errIgnore);
			for (int j = 0; j < ((numCol < row.length) ? numCol : row.length); j++) {
				result[i][j] = row[j];
			}
		}
		return result;
	}
	
	/**
	 * Установка количества столбцов в матрице
	 * по умолчанию 2
	 * @param numCol
	 */
	public void setCol(int numCol) {
		this.numCol = numCol;
	}
	
	/**
	 * Установка игнорирования ошибок в записях строк
	 * @param errIgnore
	 */
	public void setErrIgnore(boolean errIgnore) {
		this.errIgnore = errIgnore;
	}
	
	/**
	 * Разбор одной строки
	 * @param row
	 * @param errIgnore
	 * @return
	 */
	public static double[] extactRow(String row, boolean errIgnore) {
		String[] sValues = row.replace(',', '.').split("\\s+"); 
		int counter = 0;
		double [] result = new double[sValues.length];
		for(String s:sValues) {
			if ((s.matches("^[-+]?[0-9]*[.,][0-9]+(?:[eE][-+]?[0-9]+)?$"))
					|| (s.matches("^[-+]?[0-9]*"))){
				result[counter++] = Double.parseDouble(s);
			} else if (errIgnore) {
				throw new IllegalArgumentException("Ошибка при разборе строки "+row);
			}
		}
		return result; 
	}
	
	
	public static void main(String[] args) {
		String d = "123.45 35 \n67.8 34 \n45.001 55";
		
		DSVparser parser = new DSVparser(d);
		double [][] arr = null;
		try {
			arr = parser.parse();
		}catch (Exception e) {
			System.err.println(e);
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.println(Arrays.toString(arr[i]));
		}
		
	}
	
}
