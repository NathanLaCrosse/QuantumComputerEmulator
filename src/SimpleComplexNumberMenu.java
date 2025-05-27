import java.util.Scanner;

import ComplexNumberPackage.ComplexCalculator;
import ComplexNumberPackage.ComplexNumber;

public class SimpleComplexNumberMenu {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        boolean loop = true;

        while(loop) {
            System.out.println("Welcome to the Complex Number Menu!");
            System.out.println("Choose an option below:");
            System.out.println("[a] Add/subtract/multiply/divide two numbers ");
            System.out.println("[b] Find conjugate and modulus");
            System.out.println("[c] Find power");
            System.out.println("[d] Find and display roots");
            System.out.println("[e] Display first 3 powers");

            char a = in.nextLine().charAt(0);
            if(a == 'a') {
                addSubtractMultDivideMenu(in);
            }else if(a == 'b') {
                conjugateModulus(in);
            }else if(a == 'c') {
                powerMenu(in);
            }else if(a == 'd') {
                rootMenu(in);
            }else if(a == 'e') {
                displayFirstThreePowers(in);
            }

            System.out.println();
            System.out.print("Would you like to continue (y/n)? ");
            loop = in.nextLine().charAt(0) != 'n';
        }

    }

    public static void displayFirstThreePowers(Scanner in) {
        ComplexNumber a = ComplexNumber.createThroughUserInputsStringInput(in);
        ComplexNumber b = ComplexCalculator.pow(a, 2);
        ComplexNumber c = ComplexCalculator.pow(a, 3);

        ComplexVisualizer.createVisualizationOfComplexNums(new ComplexNumber[]{a,b,c});
    }

    public static void powerMenu(Scanner in) {
        ComplexNumber a = ComplexNumber.createThroughUserInputsStringInput(in);
        System.out.print("Enter power: ");
        int n = in.nextInt(); in.nextLine();

        ComplexNumber powNum = ComplexCalculator.pow(a, n);
        System.out.println(a + "^" + n + " = " + powNum);

        ComplexVisualizer.createVisualizationOfComplexNums(new ComplexNumber[]{a, powNum});
    }

    public static void rootMenu(Scanner in) {
        ComplexNumber a = ComplexNumber.createThroughUserInputsStringInput(in);
        System.out.print("Enter root: ");
        int n = in.nextInt(); in.nextLine();

        System.out.println("(" + a + ")^1/" + n + " = ");
        ComplexNumber[] roots = ComplexCalculator.roots(a, n);
        for(int i = 0; i < roots.length; i++) {
            System.out.println((i+1) + " - " + roots[i]);
        }
        ComplexVisualizer.createVisualizationOfComplexNums(roots);
    }

    public static void conjugateModulus(Scanner in) {
        ComplexNumber a = ComplexNumber.createThroughUserInputsStringInput(in);

        System.out.println("conjugate of " + a + " is " + ComplexNumber.conjugate(a));
        System.out.println("|" + a + "| = " + ComplexNumber.modulus(a));
    }

    public static void addSubtractMultDivideMenu(Scanner in) {
        ComplexNumber a = ComplexNumber.createThroughUserInputsStringInput(in);
        ComplexNumber b = ComplexNumber.createThroughUserInputsStringInput(in);

        System.out.println(a + " + " + b + " = " + ComplexNumber.add(a, b));
        System.out.println(a + " - " + b + " = " + ComplexNumber.subtract(a, b));
        System.out.println(a + " * " + b + " = " + ComplexNumber.multiply(a, b));
        System.out.println(a + " / " + b + " = " + ComplexNumber.divide(a, b));
    }
}
