package RealNumberPackage;
/**
 * This class represents a fraction with two integer numbers
 */
public class Fraction extends RealNumber {

    public static void main(String[] args) {
        Fraction f = (Fraction)RealNumber.parseRealNumber("1dsqrt2");
        Fraction g = (Fraction)RealNumber.parseRealNumber("1dsqrt2");

        System.out.println(f.multiply(g));
    }

    private RealNumber numerator, denominator;

    public Fraction(RealNumber numerator, RealNumber denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    public Fraction() {
        this(new RealDouble(), new RealDouble(1));
    }

    public RealNumber getNumerator() {
        return numerator;
    }
    public RealNumber getDenominator() {
        return denominator;
    }
    public void setNumerator(RealNumber numerator) {
        this.numerator = numerator;
    }
    public void setDenominator(RealNumber denominator) {
        this.denominator = denominator;
    }
    /**
     * Reduces the fraction to simplest terms
     */
    public void reduce() {
        for(long i = Math.round(denominator.valueOf()); i > 1; i--) {
            if(numerator.intValue() % i == 0 && denominator.intValue() % i == 0) {
                numerator = numerator.divide(new RealDouble(i));
                denominator = denominator.divide(new RealDouble(i));
                return;
            }
         }
    }
    /**
     * Returns the value of the fraction as a double.
     */
    @Override
    public double valueOf() {
        return numerator.valueOf() / denominator.valueOf();
    }
    /**
     * Negates the fraction
     */
    @Override 
    public RealNumber flipSign() {
        return new Fraction(numerator.flipSign(), denominator);
    }
    /**
     * Swaps the denominator and numerator
     */
    public RealNumber flip() {
        return new Fraction(denominator, numerator);
    }

    public String toString() {
        /*if(numerator.valueOf() == 0.0) { //this could make this look better in some cases
            return "0";
        }*/
        return numerator + " /" + denominator;
    }
    /**
     * Returns the string of the absolute value of this number
     */
    public String absoluteString() {
        /*if(numerator.valueOf() == 0.0) { //this could make this look better in some cases
            return "0";
        }*/
        return numerator.absoluteString() + "/" + denominator.absoluteString();
    }

    /**
     * Compares the two objects and returns true if they are both Fractions and are equal. Note that this calls the reduce() method of
     * Fraction.
     */
    public boolean equals(Object o) {
        if(!(o instanceof Fraction)) return false;
        Fraction f = (Fraction)o;
        reduce();
        f.reduce();
        return numerator == f.numerator && denominator == f.denominator;
    }

    @Override
    public RealNumber add(RealNumber n) {
        if(n.valueOf() == 0.0) {
            return this;
        }else if(!(n instanceof Fraction)) {
            return new RealDouble(valueOf() + n.valueOf());
        }
        Fraction f = (Fraction)n;
        RealNumber numerator1 = numerator.multiply(f.denominator);
        RealNumber numerator2 = f.numerator.multiply(denominator);
        Fraction g = new Fraction(numerator1.add(numerator2), denominator.multiply(f.denominator));
        g.reduce();
        return g;
    }
    @Override
    public RealNumber subtract(RealNumber n) {
        if(n.valueOf() == 0.0) {
            return this;
        }else if(!(n instanceof Fraction)) {
            return new RealDouble(valueOf() - n.valueOf());
        }
        Fraction f = (Fraction)n;
        RealNumber numerator1 = numerator.multiply(f.denominator);
        RealNumber numerator2 = f.numerator.multiply(denominator);
        Fraction g = new Fraction(numerator1.subtract(numerator2), denominator.multiply(f.denominator));
        g.reduce();
        return g;
    }
    @Override 
    public RealNumber multiply(RealNumber n) {
        if(n.valueOf() == Math.round(n.valueOf())) { //check to see if it is integer
            int val = (int)n.valueOf();
            return new Fraction(numerator.multiply(new RealDouble(val)), denominator);
        }else if(!(n instanceof Fraction)) {
            return new RealDouble(valueOf() * n.valueOf());
        }
        Fraction f = (Fraction)n;
        Fraction d = new Fraction(numerator.multiply(f.numerator), denominator.multiply(f.denominator));
        d.reduce();
        return d;
    }
    @Override
    public RealNumber divide(RealNumber n) {
        if(n.valueOf() == Math.round(n.valueOf())) { //check to see if it is integer
            int val = (int)n.valueOf();
            return new Fraction(numerator.divide(new RealDouble(val)), denominator);
        } else if(!(n instanceof Fraction)) {
            return new RealDouble(valueOf() / n.valueOf());
        }
        Fraction f = (Fraction)n;
        Fraction d = new Fraction(numerator.multiply(f.denominator), denominator.multiply(f.numerator));
        d.reduce();
        return d;
    }

    /**
     * Converts a String into a fraction. Before using this method make sure that the given string will have 'd' in it to denote where the
     * division is in in the fraction.
     */
    public static Fraction parseFraction(String s) throws Exception {
        int divDex = s.indexOf("d");
        if(divDex == -1) {
            throw new Exception("Cannot properly parse fraction!");
        }
        int num = Integer.parseInt(s.substring(0, divDex));
        int denom = Integer.parseInt(s.substring(divDex + 1));
        return new Fraction(new RealDouble(num), new RealDouble(denom));
    }
}
 