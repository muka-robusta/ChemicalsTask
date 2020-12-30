package com.one2story;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CUI console = new CUI();
        GeometricMathProgramming solver = new GeometricMathProgramming(
                console.readDoubleValue("P"),
                console.readDoubleValue("K"),
                console.readDoubleValue("C"),
                console.readDoubleValue("q"),
                console.readDoubleValue("alpha")
        );
        solver.getOptimist();

        double[] startN = {1,0,0,0};
        double[] startT = solver.solve(startN, 10, 1, 2);
        double startF = solver.targetFunction(startN, startT);

        start:
        for (int i = 1; i <= 3; i++)
            for (int j = 0; j <= i; j++)
                for (int k = 0; k <= j; k++)
                    for (int l = 0; l <= k; l++) {
                        double[] n = {i,j,k,l};
                        double[] t = solver.solve(n, 10, 1, 2);

                        if (t != null) {
                            double f = solver.targetFunction(n, t);
                            if (f < startF) {
                                startN = n;
                                startT = t;
                                startF = f;
                            }
                        } else continue start;
                    }

        console.out("Кількість контейнерів", startN[0]);
        console.out("Розміри контейнерів", Arrays.toString(startT));
        console.out("Вартість", startF);

    }


}
