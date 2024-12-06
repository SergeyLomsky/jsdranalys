package ru.sl.jsdranalys.kinematic;

public class Utilites {
    
    /**
     * Парметры земного эллипсоида: малая полуось
     */
    public static final double SEMIMAJORAXIS = 6378137;// ГСК2011

    /**
     * Парметры земного эллипсоида: квадрат эксцентриситета
     */
    public static final double ESQUARE = 0.00669437999014;// ГСК2011
    
    /**
     * перевод географической системы координат в геодезическую
     *
     * @param xyz координаты XYZ ГОСК
     * @return координаты BLH (углы BL в радианах)
     */
    public static double[] convertGOSKtoBLH(double[] xyz) {

        double[] result = new double[3];

        double value_r = evclideNorm(xyz[0], xyz[1], 0);
        
        if (value_r == 0) {
            result[0] = xyz[2] * Math.PI / (2 * Math.abs(xyz[2]));
            result[1] = 0;
            double sin_B = Math.sin(result[0]);
            result[2] = xyz[2] * sin_B - SEMIMAJORAXIS * Math.sqrt(1 - ESQUARE * sin_B * sin_B);
        } else {
            double value_La = Math.abs(Math.asin(xyz[1] / value_r));

            if ((xyz[0] > 0) && (xyz[1] < 0))
                result[1] = 2 * Math.PI - value_La;
            if ((xyz[0] < 0) && (xyz[1] < 0))
                result[1] = Math.PI + value_La;
            if ((xyz[0] < 0) && (xyz[1] > 0))
                result[1] = Math.PI - value_La;
            if ((xyz[0] > 0) && (xyz[1] > 0))
                result[1] = value_La;

            if ((xyz[2] == 0)) {
                result[0] = 0;
                result[2] = value_r - SEMIMAJORAXIS;
            } else {
                double value_ro = evclideNorm(xyz[0], xyz[1],xyz[2]);
                double c = Math.asin(xyz[2] / value_ro);
                double p = ESQUARE * SEMIMAJORAXIS / (2 * value_ro);
                double s1 = 0;
                double d = 1;
                double b = 0;
                double s2 = 0;
                double sinb = 0;
                double precision = 1e-25;
                while (d > precision) {
                    s1 = s2;
                    b = c + s1;
                    sinb = Math.sin(b);
                    s2 = Math.asin(p * Math.sin(2 * b) / Math.sqrt(1 - ESQUARE * sinb * sinb));
                    d = Math.abs(s2 - s1);
                }
                result[0] = b;
                sinb = Math.sin(b);
                result[2] = value_r * Math.cos(b) + xyz[2] * sinb
                        - SEMIMAJORAXIS * Math.sqrt(1 - ESQUARE * sinb * sinb);
            }
        }
        return result;
    }
    
    /**
     * перевод из геодезической в географическую систему координат
     *
     * @param blh координаты BLH (углы BL в радианах)
     * @return координаты XYZ ГОСК
     */
    public static double[] converttoBLHtoGOSK(double[] blh) {
        double sin_b = Math.sin(blh[0]);
        double value_N = SEMIMAJORAXIS / Math.sqrt(1 - ESQUARE * sin_b * sin_b);
        double value_N_H = (value_N + blh[2]) * Math.cos(blh[0]);
        return new double[] { value_N_H * Math.cos(blh[1]),
                value_N_H * Math.sin(blh[1]),
                ((1 - ESQUARE) * value_N + blh[2]) * sin_b };

    }
    
   /**
    * Эвклидова норма
    * @param a 
    * @param b
    * @param c
    * @return
    */
    public static double evclideNorm(double a, double b, double c) {
        return Math.sqrt(a*a+b*b+c*c);
    }
    
    /**
     * Эвклидова метрика 
     * Определение расстояния между двумя точками
     *
     * @param first  координаты 1-й точки
     * @param second координаты 2-й точки
     * @return расстояние
     */
    public static double calculateRadialDistanse(double[] first, double[] second) {
        return evclideNorm(first[0] - second[0], first[1] - second[1],
                first[2] - second[2]);
    }

}
