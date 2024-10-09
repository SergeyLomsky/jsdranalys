package ru.sl.jsdranalys.dsp;

import java.util.stream.IntStream;

public class MathUtilites {
	
	private static int [] MASK = {0x0,0x1,0x2,0x4,0x8,0x10,0x20,0x40,0x80,
								  0x100,0x200,0x400,0x800,0x1000,0x2000,0x4000,0x8000,
								  0x10000,0x20000,0x40000,0x80000,0x100000,0x200000,0x400000,0x800000,
								  0x1000000,0x2000000,0x4000000,0x8000000,
								  0x10000000,0x20000000,0x40000000,0x80000000};

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
	
	// изменение порядка бит для БПФ
	public static int[] countBitMirror(int n) {
		int[] baseArr = IntStream.range(0, n).toArray();
		int pow = pow2(n-1);		
		for (int i = 0; i < baseArr.length; i++) {
			int curr = baseArr[i];
			int out = 0;
			int mask = MASK[pow];
			for (int j = 0; j < pow; j++) {
				if ((curr & 1) == 1)
					out += mask;
				mask >>= 1;
				curr >>= 1;
			}
			baseArr[i] = out;
		}
		return baseArr;
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
