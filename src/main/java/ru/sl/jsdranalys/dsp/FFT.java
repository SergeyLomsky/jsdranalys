package ru.sl.jsdranalys.dsp;

import java.util.Arrays;

public class FFT {
    
    private static final double[] EXP2N_SIN = { -1.0, -0.7071067811865475, -0.3826834323650898, -0.19509032201612825,
            -0.0980171403295606, -0.049067674327418015, -0.024541228522912288, -0.012271538285719925,
            -0.006135884649154475, -0.003067956762965976, -0.0015339801862847655, -7.669903187427045E-4,
            -3.8349518757139556E-4, -1.917475973107033E-4, -9.587379909597734E-5, -4.793689960306688E-5,
            -2.396844980841822E-5, -1.1984224905069705E-5, -5.9921124526424275E-6, -2.996056226334661E-6,
            -1.4980281131690111E-6, -7.490140565847157E-7, -3.7450702829238413E-7, -1.8725351414619535E-7,
            -9.362675707309808E-8, -4.681337853654909E-8, -2.340668926827455E-8, -1.1703344634137277E-8,
            -5.8516723170686385E-9, 2.9258361585343192E-9, 1.4629180792671596E-9, 7.314590396335798E-10,
            3.657295198167899E-10, 1.8286475990839495E-10, 9.143237995419748E-11, 4.571618997709874E-11,
            2.285809498854937E-11, 1.1429047494274685E-11, 5.714523747137342E-12, 2.857261873568671E-12,
            1.4286309367843356E-12, 7.143154683921678E-13, 3.571577341960839E-13, 1.7857886709804195E-13,
            8.928943354902097E-14, 4.4644716774510487E-14, 2.2322358387255243E-14, 1.1161179193627622E-14,
            5.580589596813811E-15, 2.7902947984069054E-15, 1.3951473992034527E-15, 6.975736996017264E-16,
            3.487868498008632E-16, 1.743934249004316E-16, 8.71967124502158E-17, 4.35983562251079E-17,
            2.179917811255395E-17, 1.0899589056276974E-17, 5.449794528138487E-18, 2.7248972640692436E-18,
            1.3624486320346218E-18 };

    private static double[] EXP2N_COS = { 6.123233995736766E-17, 0.7071067811865476, 0.9238795325112867,
            0.9807852804032304, 0.9951847266721969, 0.9987954562051724, 0.9996988186962042, 0.9999247018391445,
            0.9999811752826011, 0.9999952938095762, 0.9999988234517019, 0.9999997058628822, 0.9999999264657179,
            0.9999999816164293, 0.9999999954041073, 0.9999999988510269, 0.9999999997127567, 0.9999999999281892,
            0.9999999999820472, 0.9999999999955118, 0.999999999998878, 0.9999999999997194, 0.9999999999999298,
            0.9999999999999825, 0.9999999999999957, 0.9999999999999989, 0.9999999999999998, 0.9999999999999999, 1.0,
            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
            1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };

    // шаг 4-х точечного БПФ
    private static int RSTEP = 4;
    // индексы массивов для вычисления комлексных значений
    private static int IA = 0;
    private static int IB = 1;
    private static int IC = 2;
    private static int ID = 3;

    public FFT() {
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

    public static void calculate(double[] reData, double[] imData) {

        int arrCount = reData.length;

        int halfCount = arrCount >> 1;

        int j = 0;
        for (int i = 0; i < arrCount; i++) {
            if (i < j) {
                double temp = reData[i];
                reData[i] = reData[j];
                reData[j] = temp;

                temp = imData[i];
                imData[i] = imData[j];
                imData[j] = temp;
            }

            int k = halfCount;
            while (k <= j && k > 0) {
                j -= k;
                k >>= 1;
            }
            j += k;
        }

        // расчет "бабочек"
        for (int i = 0; i < arrCount; i += RSTEP) {
            calcFourValues(reData, imData, i);
        }

        for (int count = RSTEP, oldLogCnt = 0; count < arrCount;) {
            int currCnt = count << 1;
            int logCnt = oldLogCnt + 1;
            Complex w = new Complex(EXP2N_COS[logCnt], EXP2N_SIN[logCnt]);

            for (int beginIndex = 0; beginIndex < arrCount; beginIndex += currCnt) {
                int endIndex = beginIndex + count;
                Complex wData = new Complex(1, 0);

                for (int k = 0; k < count; k++) {
                    Complex bData = new Complex(reData[beginIndex + k], imData[beginIndex + k]);
                    Complex mulResult = multiply(wData, new Complex(reData[endIndex + k], imData[endIndex + k]));

                    reData[beginIndex + k] = bData.real + mulResult.real;
                    imData[beginIndex + k] = bData.image + mulResult.image;
                    reData[endIndex + k] = bData.real - mulResult.real;
                    imData[endIndex + k] = bData.image - mulResult.image;
                    wData = multiply(wData, w);
                }
            }
            count = currCnt;
            oldLogCnt = logCnt;
        }

    }
    
    public static void  normalizeByCount(final double[] reData, final double[] imData) {
        for (int i = 0; i < reData.length; i++) {
            reData[i] = reData[i]/reData.length;
            imData[i] = imData[i]/imData.length;
        }       
    }

    public static double[] calculateAmplitude(final double[] reData, final double[] imData) {
        double [] res = new double [reData.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = Math.sqrt(reData[i]*reData[i]+imData[i]*imData[i]);
        }
        return res;      
    }
    
    private static Complex multiply(Complex first, Complex second) {
        double real = first.real * second.real - first.image * second.image;
        double image = first.real * second.image + first.image * second.real;
        return new Complex(real, image);
    }

    private static class Complex {
        public double real;
        public double image;

        public Complex(double real, double image) {
            this.real = real;
            this.image = image;
        }
    }

    public static void main(String[] args) {
        double[] re = new double[256];
        double[] im = new double[256];
        for (int i = 0; i < im.length; i++) {
            re[i] = Math.cos(i * 2 * Math.PI / 16);
            im[i] = Math.sin(i * 2 * Math.PI / 16);
        }

       // FFT fft = new FFT();
        FFT.calculate(re, im);
        for (int i = 0; i < im.length; i++) {
            System.out.println(i + " " + re[i] + " " + im[i]);
        }

    }

}
