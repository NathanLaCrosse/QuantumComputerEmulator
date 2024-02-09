import java.util.Scanner;

import RealNumberPackage.*;

/**
 * Hey btw it would be a good idea in the future to include special exceptions for when the code will break, as I know we already detect
 * that kind of stuff (most of the time, anyway)
 */

public class Testing {
    public static void main(String[] args) throws Exception {

        String[][] s1 = {
            {"3.25","2i"},
            {"-2i","1.25"}
        };
        String[][] s2 = {
            {"1","-i"},
            {"i","2"}
        };

        String[] state1 = {"sqrt2d2","-sqrt2d2i"};
        String[] state2 = {"sqrt2d2","sqrt2d2i"};

        ComplexMatrix ma1 = new ComplexMatrix(s1);
        ComplexMatrix ma2 = new ComplexMatrix(s2);

        ComplexMatrix st1 = new ComplexMatrix(state1);
        ComplexMatrix st2 = new ComplexMatrix(state2);
        st1 = ComplexMatrix.transpose(st1);

        System.out.println(ComplexMatrix.variance(ma2, st2));

        //System.out.println(ComplexMatrix.expectedValue(ma2, st1));
        //System.out.println(ComplexMatrix.variance(ma2, st1)); 

        System.exit(0);

        normalizeVector();
        System.exit(0);

        String[][] testA = {
            {"0","1dsqrt2","1dsqrt2","0"},
            {"1dsqrt2","0","0","-1dsqrt2"},
            {"1dsqrt2","0","0","1dsqrt2"},
            {"0","-1dsqrt2","1dsqrt2","0"}
        };

        ComplexMatrix at = new ComplexMatrix(testA);

        System.out.println(at);
        System.out.println();
        System.out.println(ComplexMatrix.matrixMultiply(at, at));

        System.exit(0);

        String[][] adj = {
            {"0.1","0.2","0.7"},
            {"0.3","0.6","0.1"},
            {"0.6","0.2","0.2"}
        };
        String[] stat = {"1","0","0"};

        ComplexMatrix ama = new ComplexMatrix(adj);
        ama = ComplexMatrix.matrixMultiply(ama, ama);
        ama = ComplexMatrix.matrixMultiply(ama, ama);
        ComplexMatrix s = new ComplexMatrix(stat);

        System.out.println(ComplexMatrix.matrixMultiply(ama, s));
        System.exit(0);

        String[][] a = {
            {"3+2i","5-i","2i"},
            {"0","12","6-3i"},
            {"2","4+4i","9+3i"}
        };
        String[][] b = {
            {"1","3+4i","5-7i"},
            {"10+2i","6","2+5i"},
            {"0","1","2+9i"}
        };

        String[] c = {"2","3"};
        String[] d = {"4","6","3"};

        ComplexMatrix m1 = new ComplexMatrix(a);
        ComplexMatrix m2 = new ComplexMatrix(b);

        ComplexMatrix v1 = new ComplexMatrix(c);
        ComplexMatrix v2 = new ComplexMatrix(d);

        //System.out.println(ComplexMatrix.matrixMultiply(m1,m2));
        //System.out.println(ComplexMatrix.innerProduct(m1, m2));
        //System.out.println(ComplexMatrix.findInnerProduct(v1, v2));

        //System.out.println(Math.toDegrees(ComplexMatrix.angleBetweenVector(v1, v2)));

        // ComplexMatrix tensor = ComplexMatrix.tensorProduct(m1, m2);

        // System.out.println(tensor);
        // System.out.println(tensor.getMatrix().length);
        // System.out.println(tensor.getMatrix()[0].length);

        // System.out.println(ComplexMatrix.tensorProduct(v1, v2));
        // System.out.println(ComplexMatrix.tensorProduct(ComplexMatrix.transpose(v1), ComplexMatrix.transpose(v2)));
        String[] sampleState = {"6","2","1","5","3","10"};
        ComplexMatrix state = new ComplexMatrix(sampleState);

        // ComplexMatrix bool = new ComplexMatrix(booleanMatrix);
        // System.out.println(ComplexMatrix.booleanMatrixMultiplication(bool, bool));
        // System.out.println();
        // System.out.println(ComplexMatrix.matrixMultiply(bool, state));

        System.exit(0);
    }

    public static void combiningSystems() {
        String[] combinedState = {"1d18","0","2d18","1d3","0","1d2"};

        String[][] aDynam = {
            {"0","1d6","5d6"},
            {"1d3","1d2","1d6"},
            {"2d3","1d3","0"}
        };
        String[][] bDynam = {
            {"1d3","2d3"},
            {"2d3","1d3"}
        };

        ComplexMatrix cState = new ComplexMatrix(combinedState);

        ComplexMatrix aAdj = new ComplexMatrix(aDynam);
        ComplexMatrix bAdj = new ComplexMatrix(bDynam);

        ComplexMatrix cAdj = ComplexMatrix.tensorProduct(aAdj, bAdj);

        System.out.println(cAdj.toString(true));
    }
    public static void evaluatingProbability() throws Exception {
        String[] s = {"2-i","2i","1-i","1","-2i","2"};
        ComplexMatrix state = new ComplexMatrix(s);

        for(int i = 0; i < state.getMatrix().length; i++) {
            System.out.println(ComplexMatrix.evaluateProbability(state, i));
        }
    }
    public static void normalizeVector() throws Exception{
        String[] s = {"2-3i","1+2i"};
        ComplexMatrix v = new ComplexMatrix(s);

        System.out.println(ComplexMatrix.norm(v));
        System.out.println(ComplexMatrix.normalizeVector(v));
    }
}