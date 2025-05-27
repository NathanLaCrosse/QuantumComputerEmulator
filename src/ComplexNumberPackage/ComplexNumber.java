package ComplexNumberPackage;
import java.util.Scanner;

import RealNumberPackage.*;

/**
 * The ComplexNumber class stores two double values, representing the real and imaginary part of a complex
 * number. It contains methods to add and multiply these kinds of numbers.
 */

public class ComplexNumber {
    private RealNumber realPart, imaginaryPart;

    public ComplexNumber(RealNumber realPart, RealNumber imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }
    public ComplexNumber(double real, double imaginary) {
        this(new RealDouble(real), new RealDouble(imaginary));
    }
    public ComplexNumber(PolarCoordinate polar) {
        this.realPart = new RealDouble(polar.getP() * Math.cos(polar.getTheta()));
        this.imaginaryPart = new RealDouble(polar.getP() * Math.sin(polar.getTheta()));
    }
    public ComplexNumber() {
        this(new RealDouble(), new RealDouble());
    }

    public RealNumber realPart() {
        return realPart;
    }
    public RealNumber imaginaryPart() {
        return imaginaryPart;
    }

    public String toString() {
        String op = imaginaryPart.valueOf() < 0 ? " - " : " + ";
        return realPart() + op + imaginaryPart().absoluteString() + " i";
    }
    public String toStringRealOnly() {
        return realPart + " ";
    }
    public ComplexNumber clone() {
        return new ComplexNumber(realPart, imaginaryPart);
    }
    public boolean equals(Object o) {
        if(!(o instanceof ComplexNumber)) return false;
        ComplexNumber c = (ComplexNumber)o;
        return realPart.equals(c.realPart) && imaginaryPart.equals(c.imaginaryPart);
    }

    /**
     * Returns a new ComplexNumber object that stores the value of a + b
     */
    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.realPart().add(b.realPart()), a.imaginaryPart().add(b.imaginaryPart()));
    }
    /** 
     * Returns a new ComplexNumber object that stores the value of a - b
    */
    public static ComplexNumber subtract(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.realPart().subtract(b.realPart()), a.imaginaryPart().subtract(b.imaginaryPart()));
    }
    /**
     * Returns a new ComplexNumber object that stores the value of a * b
     */
    public static ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        RealNumber realPart = a.realPart().multiply(b.realPart()).subtract(a.imaginaryPart().multiply(b.imaginaryPart()));
        RealNumber imaginaryPart = a.realPart().multiply(b.imaginaryPart()).add(a.imaginaryPart().multiply(b.realPart()));
        return new ComplexNumber(realPart, imaginaryPart);
    }
    /**
     * Returns a new ComplexNumber object that stores the value of a / b
     */
    public static ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        RealNumber denominator = b.realPart().multiply(b.realPart()).add(b.imaginaryPart().multiply(b.imaginaryPart()));
        if(denominator.valueOf() == 0) {
            System.out.println("Division by zero has occured!");
            System.exit(0);
        }
        RealNumber realPart = a.realPart().multiply(b.realPart()).add(a.imaginaryPart().multiply(b.imaginaryPart())).divide(denominator);
        RealNumber imaginaryPart = a.imaginaryPart().multiply(b.realPart()).subtract(a.realPart().multiply(b.imaginaryPart())).divide(denominator);
        return new ComplexNumber(realPart, imaginaryPart);
    }
    /**
     * Returns the modulus of a ComplexNumber. given complex number c,
     * modulus = |c| = sqrt(a^2 + b^2)
     * 
     * Note: this is the length of the vector created in the complex plane
     */
    public static double modulus(ComplexNumber a) {
        return Math.sqrt(a.realPart().multiply(a.realPart()).add(a.imaginaryPart().multiply(a.imaginaryPart())).valueOf());
    }
    /**
     * Returns the modulus squared of a ComplexNumber, useful for converting complex numbers into probabilities.
     */
    public static double modulusSquare(ComplexNumber a) {
        return a.realPart.multiply(a.realPart).add(a.imaginaryPart.multiply(a.imaginaryPart)).valueOf();
    }
    /**
     * Returns the conjuate of the ComplexNumber
     */
    public static ComplexNumber conjugate(ComplexNumber a) {
        return new ComplexNumber(a.realPart(), a.imaginaryPart().flipSign());
    }
    /**
     * Returns the angle the ComplexNumber makes with the origin
     *  - Useful when switching with polar coordinates
     */
    public static double determineAngleFromCenter(ComplexNumber a) {
        return Math.atan2(a.imaginaryPart().valueOf(), a.realPart().valueOf());
    }

    /**
     * Uses the Scanner in to create a new ComplexNumber object using number inputs
     */
    public static ComplexNumber createThroughUserInputsNumInputs(Scanner in) {
        System.out.print("Input real part of number: ");
        RealNumber realPart = RealNumber.parseRealNumber(in.nextLine().trim());
        System.out.print("Input imaginary part of number: ");
        RealNumber imaginaryPart = RealNumber.parseRealNumber(in.nextLine().trim());
        System.out.println();
        return new ComplexNumber(realPart, imaginaryPart);
    }
    /**
     * Uses the Scanner to create a new ComplexNumber using a string input containing the number
     */
    public static ComplexNumber createThroughUserInputsStringInput(Scanner in) {
        System.out.print("Input the number in a + bi form: ");
        String n = in.nextLine();
        System.out.println();
        return parseComplexNumber(n);
    }
    
    public static ComplexNumber parseComplexNumber(String s) {
        s.trim();

        if(s.charAt(0) == '(') {
            s.substring(1, s.length() - 1);
        }

        int indexOfOperation = s.indexOf("+",1); //try to find + sign
        if(indexOfOperation == -1) {
            indexOfOperation = s.indexOf("-",1);
        }
        if(indexOfOperation == -1) {
            return parseOneComponent(s);
        }

        String r = s.substring(0, indexOfOperation);
        String i = s.substring(indexOfOperation, s.length() - 1); 

        if(i.charAt(0) == '+') {i = i.substring(1);}

        if(i.indexOf(" ") != -1) {
            //remove blank space in number
            i = i.substring(0, i.indexOf(" ")) + i.substring(i.lastIndexOf(" ") + 1);
        }

        if(i.length() == 0 || i.equals("-")) {
            i += "1";
        }

        return new ComplexNumber(RealNumber.parseRealNumber(r), RealNumber.parseRealNumber(i));
    }
    private static ComplexNumber parseOneComponent(String s) {
        s.trim();
        boolean isRealPart = s.indexOf("i") == -1;
        if(!isRealPart) {
            //remove i from parsing
            s = s.substring(0, s.length() - 1);
            //add one if empty
            if(s.length() == 0) {
                s += "1";
            }
            //make sure it isnt just a negative
            else if(s.equals("-")) {
                s += "1";
            }
        } 
        RealNumber val = RealNumber.parseRealNumber(s);
        if(isRealPart) {
            return new ComplexNumber(val, new RealDouble());
        }
        return new ComplexNumber(new RealDouble(), val);
    }
}