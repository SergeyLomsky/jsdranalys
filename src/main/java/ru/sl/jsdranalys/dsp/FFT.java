package ru.sl.jsdranalys.dsp;

import java.util.Arrays;

public class FFT {
	
	//шаг 4-х точечного БПФ
	private static int RSTEP = 4;
	//индексы массивов для вычисления комлексных значений
	private static int IA = 0;
	private static int IB = 1;
	private static int IC = 2;
	private static int ID = 3;
	
	FFT(){
		
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
	private static void calcTwoValues (final double[] rValues, final double[] iValues){
		
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
	private static void calcFourValues (final double[] rValues, final double[] iValues, int pos){
		
		final double[] rV = Arrays.copyOfRange(rValues, pos, pos + RSTEP);
		final double[] iV = Arrays.copyOfRange(iValues, pos, pos + RSTEP);
		
		//A+B+C+D
		rValues[pos + IA] = rV[IA] + rV[IB] + rV[IC] + rV[ID];
		iValues[pos + IA] = iV[IA] + iV[IB] + iV[IC] + iV[ID];
		//A-jB-C+jD
		rValues[pos + IB] = rV[IA] + iV[IB] - rV[IC] - iV[ID];
		iValues[pos + IB] = iV[IA] - rV[IB] - iV[IC] + rV[ID];
		//A-B+C-D
		rValues[pos + IC] = rV[IA] - rV[IB] + rV[IC] - rV[ID];
		iValues[pos + IC] = iV[IA] - iV[IB] + iV[IC] - iV[ID];
		//A+jB-C-jD
		rValues[pos + ID] = rV[IA] - iV[IB] - rV[IC] + iV[ID];
		iValues[pos + ID] = iV[IA] + rV[IB] - iV[IC] - rV[ID];	
	}
	
	

}
