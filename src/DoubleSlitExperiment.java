import java.util.Scanner;

public class DoubleSlitExperiment {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        boolean loop = true;
        while(loop) {
            try {
                System.out.println("Welcome to the Double Slit Experiment Builder!");
                System.out.print("Enter the number of slits: ");
                int numSlits = Integer.parseInt(in.nextLine().trim());
                System.out.println();
                String[] slitProbs = new String[numSlits];
                System.out.println("Enter the probability to travel to each slit from the start. Probabilities, or the modulus squared of them, must add up to 1. The program will not do this for you.");
                for(int i = 0; i < numSlits; i++) {
                    System.out.print("Enter a probability: ");
                    slitProbs[i] = in.nextLine();
                    ComplexNumber.parseComplexNumber(slitProbs[i]); // try to crash if not possible, note that we need it as a string for later
                }
                System.out.print("Next, enter the number of end nodes for the experiment: ");
                int numNodes = Integer.parseInt(in.nextLine().trim());
                String[][] nodeProbs = new String[numSlits][numNodes];
                for(int i = 0; i < numSlits; i++) {
                    System.out.println("Enter the probabilities of going from slit " + (i+1) + " to each node (the total will need to combine to 1, not checked for you):");
                    for(int k = 0; k < numNodes; k++) {
                        System.out.print("Enter the probability to go from slit " + (i + 1) + " to node " + (k + 1) + ": ");
                        nodeProbs[i][k] = in.nextLine();
                        ComplexNumber.parseComplexNumber(nodeProbs[i][k]); // try to crash if not possible, note that we need it as a string for later
                    }
                }
                String[] s = new String[1 + numSlits + numNodes];
                s[0] = "1";
                for(int i = 1; i < s.length; i++) {
                    s[i] = "0";
                }

                String[][] adj = new String[1 + numSlits + numNodes][1 + numSlits + numNodes];
                for(int i = 0; i < adj.length; i++) {
                    adj[0][i] = "0";
                }
                int rowDex = 1;
                for(int i = 0; i < numSlits; i++) {
                    adj[rowDex][0] = slitProbs[i];
                    for(int k = 1; k < adj.length; k++) {
                        adj[rowDex][k] = "0";
                    }
                    rowDex++;
                }
                for(int i = 0; i < nodeProbs[0].length; i++) {
                    adj[rowDex][0] = "0";
                    int colDex = 1;
                    for(int k = 0; k < nodeProbs.length; k++) {
                        adj[rowDex][colDex] = nodeProbs[k][i];
                        colDex++;
                    }
                    while(colDex < adj.length) {
                        if(colDex == rowDex) {
                            adj[rowDex][colDex] = "1";
                        }else {
                            adj[rowDex][colDex] = "0";
                        }
                        colDex++;
                    }
                    rowDex++;
                }

                ComplexMatrix state = new ComplexMatrix(s);
                ComplexMatrix adjacency = new ComplexMatrix(adj);

                System.out.println("Starting State:");
                System.out.println(state);
                System.out.println();
                System.out.println("Adjacency Matrix:");
                System.out.println(adjacency);
                System.out.println();
                ComplexMatrix s1 = ComplexMatrix.matrixMultiply(adjacency, state);
                System.out.println("State after 1 time tick:");
                System.out.println(s1);
                System.out.println();
                ComplexMatrix s2 = ComplexMatrix.matrixMultiply(adjacency, s1);
                System.out.println("State after 2 time ticks (experiment complete):");
                System.out.println(s2);

                System.out.println();
                System.out.println("Would you like to continue? (y/n): ");
                loop = in.nextLine().toLowerCase().charAt(0) == 'y';
            }catch (NumberFormatException e) {
                System.out.println("An error recieving the input has occured! Unfortunately, the menu will now repeat.");
            }
        }
    }
}
