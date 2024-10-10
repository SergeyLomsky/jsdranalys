package ru.sl.jsdranalys.dsp;

import java.util.Arrays;

public class FFT {

	// шаг 4-х точечного БПФ
	private static int RSTEP = 4;
	// индексы массивов для вычисления комлексных значений
	private static int IA = 0;
	private static int IB = 1;
	private static int IC = 2;
	private static int ID = 3;
	private static double [] EXP2N_SIN = new double [63];
	private static double [] EXP2N_COS = new double [63];
	private static int [] MASK = new int [63];


	FFT() {
		//Инициализация расчетных данных
		int k = 1;
		for (int i = 0; i < 32; i++) {
			double arg = 2*Math.PI/k;
			EXP2N_SIN[i] = -Math.sin(arg);
			EXP2N_COS[i] = Math.cos(arg);
			MASK[i] = k;
			k <<= 1;
		}
		double old = EXP2N_SIN[31];
		for (int i = 32; i < 63; i++) {
			EXP2N_SIN[i] = old/2;
			old = EXP2N_SIN[i];
			EXP2N_COS[i] = 1;
			MASK[i] = k;
			k <<= 1;
		}

	}

	/*
	 * БПФ по основанию 2
	 * 
	 * [ai , ar]        [ar+br , ai+bi] 
	 *          \      /
	 *           ------
	 *           |    |
	 *           ------
	 *          /      \
	 * [bi , br]        [ar-br , ai-bi]
	 */
	private static void calcTwoValues(final double[] rValues, final double[] iValues) {

		final double rA = rValues[IA];
		final double iA = iValues[IA];
		final double rB = rValues[IB];
		final double iB = iValues[IB];
		
		rValues[IA] = rA + rB;
		rValues[IB] = rA - rB;
		iValues[IA] = iA + iB;
		iValues[IB] = iA - iB;
	}
	
	/*
	 * БПФ по основанию 4
	 * 
	 *  A            A+B+C+D    
	 *    \        /
	 *      ------ 
	 * B--|       |--A-jB-C+jD
	 * C--|       |--A-B+C-D
	 *      ------
	 *    /       \
	 *  D           A+jB-C-jD
	 */
	private static void calcFourValues(final double[] rValues, final double[] iValues, int pos) {

		final double[] rV = Arrays.copyOfRange(rValues, pos, pos + RSTEP);
		final double[] iV = Arrays.copyOfRange(iValues, pos, pos + RSTEP);
		// Для ускорения работы оперфции с комплексными числами вынесены в
		// отдельный метод
		rValues[pos + IA] = rV[IA] + rV[IB] + rV[IC] + rV[ID];// A+B+C+D
		iValues[pos + IA] = iV[IA] + iV[IB] + iV[IC] + iV[ID];
		rValues[pos + IB] = rV[IA] + iV[IB] - rV[IC] - iV[ID];// A-jB-C+jD
		iValues[pos + IB] = iV[IA] - rV[IB] - iV[IC] + rV[ID];
		rValues[pos + IC] = rV[IA] - rV[IB] + rV[IC] - rV[ID];// A-B+C-D
		iValues[pos + IC] = iV[IA] - iV[IB] + iV[IC] - iV[ID];
		rValues[pos + ID] = rV[IA] - iV[IB] - rV[IC] + iV[ID];// A+jB-C-jD
		iValues[pos + ID] = iV[IA] + rV[IB] - iV[IC] - rV[ID];
	}
	
	public void calculate(double [] reData, double [] imData) {
		
		int arrCount = reData.length;
		//реверс значений (зеркальный бинарный поворот индекса)
		int pow = MathUtilites.pow2(arrCount-1);		
		for (int i = 0; i < arrCount >> 1; i++) {
			int curr = i;
			int out = 0;
			int mask = MASK[pow-1];
			for (int j = 0; j < pow; j++) {
				if ((curr & 1) == 1)
					out += mask;
				mask >>= 1;
				curr >>= 1;
			}
			double temp = reData[i];
			reData[i] = reData[out];
			reData[out] = temp;
			temp = imData[i];
			imData[i] = imData[out];
			imData[out] = temp;
		}
		//расчет "бабочек"
		for (int i = 0; i < arrCount; i += RSTEP) {
			calcFourValues(reData, imData, i);
		}
		
		
	}

	public static void main(String[] args) {
		double [] re = {1,2,3,4,5,6,7,8};
		double [] im = {1,2,3,4,5,6,7,8};
		FFT fft = new FFT();
		fft.calculate(re, im);
		System.out.println(Arrays.toString(re));
		System.out.println(Arrays.toString(im));
	
	}

}
