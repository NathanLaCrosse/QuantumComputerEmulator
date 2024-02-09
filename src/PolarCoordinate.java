import java.util.Scanner;

/**
 * This class represents a complex number as a vector with magnitude p and angle theta
 */

class PolarCoordinate {
    private double p, theta; 

    public PolarCoordinate(double magnitude, double angle) {
        this.p = magnitude;
        this.theta = angle;
    }
    public PolarCoordinate(ComplexNumber complex) {
        this.p = ComplexNumber.modulus(complex);
        this.theta = ComplexNumber.determineAngleFromCenter(complex);
    }

    public double getP() {
        return p;
    }
    public double getTheta() {
        return theta;
    }

    public String toString() {
        return "(" + p + ", " + theta + ")";
    }

    /**
     * Returns a polar coordinate object with the value of a * b
     */
    public static PolarCoordinate multiplyPolarCoordinates(PolarCoordinate a, PolarCoordinate b) {
        return new PolarCoordinate(a.getP() * b.getP(), a.getTheta() + b.getTheta());
    }
    /**
     * Returns a polar coordinate object with the value of a / b
     */
    public static PolarCoordinate dividePolarCoordinates(PolarCoordinate a, PolarCoordinate b) {
        if(b.getP() == 0) {System.out.println("Divide by 0 error when dividing polar coordinates!"); System.exit(0);}
        return new PolarCoordinate(a.getP() / b.getP(), a.getTheta() - b.getTheta());
    }
    /**
     * Returns a polar coordinate to the nth power
     */
    public static PolarCoordinate raiseToPower(PolarCoordinate a, int n) {
        return new PolarCoordinate(Math.pow(a.getP(), n), n * a.getTheta());
    }
    /**
     * Returns an array filled with the possible roots of a polar coordinate number a
     * @param a is raised to the 1/n power
     */
    public static PolarCoordinate[] findRoots(PolarCoordinate a, int n) {
        PolarCoordinate[] roots = new PolarCoordinate[n];
        double newMagnitude = Math.pow(a.getP(), 1.0/n);
        double startingAngle = (1.0/n)*a.getTheta();
        double angleSpacing = (1.0/n)*2*Math.PI;
        for(int i = 0; i < n; i++) {
            roots[i] = new PolarCoordinate(newMagnitude, startingAngle + i * angleSpacing);
        }
        return roots;
    }

    /**
     * Uses the Scanner in to create a new PolarCoordinate object
     */
    public static PolarCoordinate createThroughUserInputs(Scanner in) {
        System.out.print("Input magnitude(p): ");
        double p = in.nextDouble();
        in.nextLine();
        System.out.print("Input theta: ");
        double theta = in.nextDouble();
        in.nextLine();
        System.out.println();
        return new PolarCoordinate(p, theta);
    }
}
