package ru.sl.jsdranalys.dsp;

import java.util.Arrays;

public class FFT {
	
	FFT(){
		
	}
	
	/*
	 * [ai , ar]        [ar+br , ai+bi] 
	 *          \      /
	 *           ------
	 *           |    |
	 *           ------
	 *          /      \
	 * [bi , br]        [ar-br , ai-bi]
	 */
	
	private static void calcTwoValues (final double[] rValues, final double[] iValues){
		
		final double r0 = rValues[0];
		final double i0 = iValues[0];
		final double r1 = rValues[1];
		final double i1 = iValues[1];
		
		rValues[0] = r0 + r1;
		rValues[1] = r0 - r1;
		iValues[0] = i0 + i1;
		iValues[1] = i0 - i1;
	}
	
	private static void calcFourValues (final double[] rValues, final double[] iValues, int pos){
		
		final double[] rV = Arrays.copyOfRange(rValues, pos, pos + 4);
		final double[] iV = Arrays.copyOfRange(iValues, pos, pos + 4);
		
		rValues[pos + 0] = rV[0] + rV[1] + rV[2] + rV[3];
		rValues[pos + 1] = rV[0] - rV[2] + iV[1] - iV[3];
		rValues[pos + 2] = rV[0] - rV[1] + rV[2] - rV[3];
		rValues[pos + 3] = rV[0] - rV[2] + iV[3] - iV[1];
		iValues[pos + 0] = iV[0] + iV[1] + iV[2] + iV[3];
		iValues[pos + 1] = iV[0] - iV[2] + rV[3] - rV[1];
		iValues[pos + 2] = iV[0] - iV[1] + iV[2] - iV[3];
		iValues[pos + 3] = iV[0] - iV[2] + rV[1] - rV[3];

		
	}

}
