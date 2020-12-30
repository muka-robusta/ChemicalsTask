package com.one2story;

import java.util.Arrays;

public class GeometricMathProgramming {

    double P, K, C, q, alpha;
    private double accuracy = 1;

    public GeometricMathProgramming(double P, double K, double C, double q, double alpha) {
        this.P = P;
        this.K = K;
        this.C = C;
        this.q = q;
        this.alpha = alpha;
    }

    public void getOptimist(){
        P = P * (1 - Math.sqrt(1 / alpha - 1));
        C = C * (1 - Math.sqrt(Math.log(Math.pow(alpha,-2))));
        q = q * (1 - Math.sqrt(2 * (1/alpha - 1)));
    }

    public void getPessimist() {
        P = P * (1 + Math.sqrt(1 / alpha - 1));
        C = C * (1 + Math.sqrt(Math.log(Math.pow(alpha, -2))));
        q = q * (1 + Math.sqrt(2 * (1 / alpha - 1)));
    }

    /**
     *  f - target function
     *  f = Cn * 2nq(t1*t2 + t3*t2 + t1*t3) - q(n1*t1*t2 + n2*t3*t2 + n3*t1*t3) -> min
     *
     * @param n
     * @param t
     * @return target function - f(x)
     */
    public double targetFunction(double[] n, double[] t) {
        return C * n[0] + 2.0 * n[0] * q * (t[0] * t[1] + t[1] * t[2] + t[0] * t[2])
                - q * (n[1] * t[0] * t[1] + n[2] * t[0] * t[2] + n[3] * t[1] * t[2]);
    }

    /**
     *  P - penalty function
     *  P = sum_on_i(g)
     * @param n
     * @param t
     * @param p
     * @return penalty Function - P(x)
     */
    public double penaltyFunction(double[] n, double[] t, double p) {
        double penaltyValue = Math.pow(Math.max(0,-(n[0]*t[0]*t[1]*t[2]-P)),p);
        penaltyValue += Math.pow(Math.max(0,-(K - (n[1] * t[0] * t[1] + n[2] * t[0] * t[2] + n[3] * t[1] * t[2]))),p);
        penaltyValue += Math.pow(Math.max(0,-t[0]),p) + Math.pow(Math.max(0,-t[1]),p) + Math.pow(Math.max(0,-t[2]),p);
        return penaltyValue;
    }

    /**
     *  Pass from target function to fixed Function
     *  f -> F
     * @param n
     * @param t
     * @param r
     * @param p
     * @return f(x) + P(x)
     */
    public double fixedTask(double[] n, double[] t, double r, double p)
    {
        return targetFunction(n, t) + r * penaltyFunction(n, t, p);
    }

    public double[] countFunctionGradient(double[] n, double[] t, double r, double p) {
        double[] gradient = new double[3];
        double step = 0.001;
        double[] shift = Arrays.copyOf(t, 3);
        for (int i = 0; i < 3; i++) {
            shift[i] += step;
            gradient[i] = (fixedTask(n, shift, r, p) - fixedTask(n, t, r, p)) / step;
        }
        return gradient;
    }

    /*
        Count Euclid`s norm
     */
    public static double norm(double[] x) {
        double norm = 0;
        for (double current : x)
            norm += Math.pow(current, 2);
        return Math.sqrt(norm);
    }

    /**
     * Optimize by gradient descent of size of the box
     * @param n
     * @param t
     * @param r
     * @param p
     * @param step
     * @return array of optimized t
     */
    public double[] gradientDescent(double[] n, double[] t, double r, double p, double step) {
        int counter = 0;
        double[] fixedT = Arrays.copyOf(t, t.length);
        double[] gradient;

        do {
            gradient = countFunctionGradient(n, fixedT, r, p);

            for (int i = 0; i < fixedT.length; i++)
                fixedT[i] -= step * gradient[i];

        } while(norm(gradient) > 1 && counter++ < 1000);
        return fixedT;
    }

    public double[] solve(double[] n, double r1, double p, double beta) {
        double[] t = {1, 1, 1};
        int count = 1;

        while (count++ < 51) {
            t = gradientDescent(n, t, r1, p, 0.0000001);
            if (r1 * penaltyFunction(n, t, p) < accuracy) return t;
            r1 *= beta;
        }

        return null;
        // throw new RuntimeException("Cant find solution");
    }

}
