import java.util.Scanner;

public class QuantumBasic {
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        transitionMenu(in);
        //pointsMenu(in);
    }
    private static void pointsMenu(Scanner in) throws Exception{
        System.out.print("Enter how many points the particle will occupy: ");
        try {
            int numPoints = Integer.parseInt(in.nextLine());
            System.out.println();
            String[] k = new String[numPoints];
            for(int i = 0; i < k.length; i++) {
                System.out.print("Enter the amplitude for point " + (i+1) + ": ");
                k[i] = in.nextLine().trim();
            }
            System.out.println();
            System.out.println("Finding probabilities for the particle being found at a certain point...");
            System.out.println();
            ComplexMatrix ket = new ComplexMatrix(k);
            for(int i = 0; i < ket.getMatrix().length; i++) {
                System.out.println("Probability of finding at point "+(i+1)+": " + ComplexMatrix.evaluateProbability(ket, i));
            }
        }catch(NumberFormatException e) {
            System.out.println("An incorrect input was detected, resetting menu...");
            pointsMenu(in);
            return;
        }
    }
    private static void transitionMenu(Scanner in) throws Exception{
        System.out.println("Enter the starting state: ");
        ComplexMatrix ket = parseKet(in);
        System.out.println();
        System.out.println("Enter the ending state: ");
        ComplexMatrix bra = parseKet(in);
        ComplexNumber innerProduct = ComplexMatrix.innerProduct(bra,ket);
        double divisor = ComplexMatrix.norm(bra) * ComplexMatrix.norm(ket);
        ComplexNumber transitionAmplitude = ComplexNumber.divide(innerProduct, new ComplexNumber(divisor, 0));
        System.out.println();
        System.out.println("Transition amplitude is " + transitionAmplitude);
    }
    private static ComplexMatrix parseKet(Scanner in) {
        System.out.print("Enter the number of elements in the ket: ");
        try {
            int numElements = Integer.parseInt(in.nextLine().trim());
            String[] k = new String[numElements];
            for(int i = 0; i < k.length; i++) {
                System.out.print("Enter the value for element " + (i+1) + ": ");
                k[i] = in.nextLine().trim();
            }
            return new ComplexMatrix(k);
        }catch(NumberFormatException e) {
            e.printStackTrace();
            System.out.println("An incorrect input has been detected! Cannot create ket!");
            return null;
        }
    }
}
 