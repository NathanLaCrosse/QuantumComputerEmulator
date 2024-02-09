package RealNumberPackage;
/**
 * This class is used to represent all kinds of real numbers. Although we could use just doubles, this is helpful for properly representing
 * things like fractions and square roots.
 */
public abstract class RealNumber {
    public static void main(String[] args) {
        String t1 = "sqrt5";
        String t2 = "-1dsqrt2";
        String t3 = "3.24";

        System.out.println(parseRealNumber(t1));
        System.out.println(parseRealNumber(t2));
        System.out.println(parseRealNumber(t3));
    }

    public abstract double valueOf();
    public int intValue() {
        return (int)valueOf();
    }
    public abstract RealNumber flipSign();

    public RealNumber add(RealNumber n) {
        if(n instanceof Fraction) return n.add(this);
        return new RealDouble(valueOf() + n.valueOf());
    }
    public RealNumber subtract(RealNumber n) {
        if(n instanceof Fraction) return n.flipSign().add(this);
        return new RealDouble(valueOf() - n.valueOf());
    }
    public RealNumber multiply(RealNumber n) {
        if(n instanceof SquareRoot) return n.multiply(this);
        if(n instanceof Fraction) return n.multiply(this);
        return new RealDouble(valueOf() * n.valueOf());
    }
    public RealNumber divide(RealNumber n) {
        if(n instanceof Fraction) return ((Fraction)n).flip().multiply(this);
        return new RealDouble(valueOf() / n.valueOf());
    }

    @Override
    public String toString() {
        return String.format("%7.3f", valueOf());
    }
    /**
     * Returns the string of the absolute value of this number
     */
    public String absoluteString() {
        return String.format("%7.3f", Math.abs(valueOf()));
    }

    public static RealNumber parseRealNumber(String s) {
        int divDex = s.indexOf("d");
        if(divDex != -1) {
            return new Fraction(parseRealNumber(s.substring(0, divDex)), parseRealNumber(s.substring(divDex + 1)));
        }
        int sqrtDex = s.indexOf("sqrt"); 
        if(sqrtDex != -1) {
            String cof = s.substring(0, sqrtDex);
            RealNumber coeff = new RealDouble(1);
            if(cof.length() > 0) {
                coeff = parseRealNumber(cof);
            }
            return new SquareRoot(coeff, parseRealNumber(s.substring(sqrtDex + 4)));
        }
        if(s.equals("-")) {s+="1";}
        return new RealDouble(Double.parseDouble(s));
    }
}
