package ru.sl.jsdranalys.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс перевода массива данных хранящихся в формате DSV
 * в массив double[][]
 * 
 * @author SergeyLomsky
 */
public class DSVparser {
	
	private String[] strArray;
	
	private boolean errIgnore = false;
	
	private int numCol = 2;
	
	/**
	 * парсер из данных в памяти
	 * @param inData
	 */
	public DSVparser(String inData) {
		this.strArray = inData.split("\n");	
	}
	
	/**
	 * парсер из данных хранящихся в памяти
	 * @param file
	 * @throws IOException
	 */
	public DSVparser(File file) throws IOException {
		List<String> rows = new ArrayList<String>();
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			rows.add(scanner.nextLine());
		}
		strArray = (String[]) rows.toArray();
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
		String[] sValues = row.trim().replace(',', '.').split("\\s+"); 
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
	
}
