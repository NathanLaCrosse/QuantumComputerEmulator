import java.util.*;

import ComplexNumberPackage.ComplexMatrix;

public class MarbleExperiment {
    public static void main(String[] args) throws Exception {

        String[][] m = {
            {"0","1dsqrt2","1dsqrt2","0"},
            {"1dsqrt2","0","0","-1dsqrt2"},
            {"1dsqrt2","0","0","1dsqrt2"},
            {"0","-1dsqrt2","1dsqrt2","0"}
        };

        ComplexMatrix adj = new ComplexMatrix(m);

        String[] v = {"1","0","0","0"};

        ComplexMatrix state = new ComplexMatrix(v);

        printSeveralStates(adj, state, 5);

        System.exit(0);

        Scanner in = new Scanner(System.in);
        deterministicMenu(in);
        in.close();
    }
    
    private static void printSeveralStates(ComplexMatrix m, ComplexMatrix v, int numStates) throws Exception{
        System.out.println("Orignal State: " + v);
        System.out.println("Adjacency Matrix: " + m);
        System.out.println();
        ComplexMatrix state = v;
        for(int i = 0; i < numStates; i++) {
            state = ComplexMatrix.matrixMultiply(m, state);
            System.out.println("State at t + " + (i + 1) + ": ");
            System.out.println(state);
        }
    }

    private static void deterministicMenu(Scanner in) throws Exception{
        int stateLength = 0;
        System.out.print("Enter the size of the vector: ");
        try {
            stateLength = Integer.parseInt(in.nextLine());
        }catch (NumberFormatException e) {
            System.out.println("You have to enter a number!");
            return;
        }

        ComplexMatrix m = createBooleanAdjacencyMatrix(in, stateLength);
        ComplexMatrix v = enterVector(in, stateLength);

        System.out.println("State: " + v);
        System.out.println("Dynamics: " + m);
        System.out.println();

        int totalTicks = 0;
        String option = "y";
        ComplexMatrix currentState = ComplexMatrix.matrixMultiply(m, v);

        while(option.toLowerCase().charAt(0) == 'y') {
            totalTicks++;
            System.out.println("State after " + totalTicks + " time tick: ");
            System.out.println(currentState);
            System.out.print("Continue? (y/n): ");
            option = in.nextLine();
            currentState = ComplexMatrix.matrixMultiply(m, currentState);
        }

    }
    private static ComplexMatrix createBooleanAdjacencyMatrix(Scanner in, int size) {
        System.out.println("Entering in adjacency matrix!");
        String[][] booleanMatrix = new String[size][size];
        for(int i = 0; i < size; i++) {
            System.out.println("Please enter a chain of " + size + " numbers that are either 0 or 1 in one combined number:");
            String input = in.nextLine();
            for(int k = 0; k < size; k++) {
                booleanMatrix[i][k] = "" + input.charAt(k);
            }
        }
        return new ComplexMatrix(booleanMatrix);
    }
    private static ComplexMatrix enterVector(Scanner in, int size) {
        System.out.println("Entering in vector!");
        String[] v = new String[size];
        for(int i = 0; i < size; i++) {
            System.out.println("[" + (i+1) + "] Enter a number (can be complex): ");
            v[i] = in.nextLine().trim(); // this can VERY easily go wrong, would not be a bad idea to include try/catch
        }
        return new ComplexMatrix(v);
    }
}
