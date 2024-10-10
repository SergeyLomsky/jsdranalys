package ru.sl.jsdranalys.dsp;

import java.util.stream.IntStream;

public class MathUtilites {
	

	/*
	 * предполагается: класс Utilites для размещения статических методов с
	 * элементарными функциями класс FastForierTransformer - быстрое преобразование
	 * Фурье класс MatrixDetermination - решение матрицы методом Гаусса, нахождение
	 * детерминанта
	 */
	// быстрая операция определения масимальной
	// степени числа по основанию 2
	public static int pow2(int num) {
		if (num == 0)
			return 0;
		int res = 0;
		int mask = 1;
		for (int i = 0; i < 31; i++) {
			if (((num & mask) != 0))
				res = i;
			mask <<= 1;
		}
		return res + 1;
	}
	

	// вывод 32-х разрядного числа в бинарном виде
	public static void binPrint(int num) {
		String s = "";
		int mask = 0x80000000;
		for (int i = 0; i < 32; i++) {
			if ((num & mask) != 0) {
				s += "1";
			} else {
				s += 0;
			}
			mask >>>= 1;
		}
		System.out.println(s);
	}



}
