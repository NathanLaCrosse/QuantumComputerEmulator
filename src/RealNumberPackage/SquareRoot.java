package RealNumberPackage;
/**
 * This class represents a square root with a scalar constant.
 */
public class SquareRoot extends RealNumber {

    public static void main(String[] args) {
        RealNumber s = RealNumber.parseRealNumber("1dsqrt2");
        RealNumber r = RealNumber.parseRealNumber("1dsqrt8");

        System.out.println(s.multiply(r));
    }

    private RealNumber coeff;
    private RealNumber arg;

    public SquareRoot(RealNumber coeff, RealNumber arg) {
        this.coeff = coeff;
        this.arg = arg;
    }
    public SquareRoot(RealNumber arg) {
        this(new RealDouble(1), arg);
    }
    public SquareRoot() {
        coeff = new RealDouble(1);
        arg = new RealDouble();
    }

    @Override 
    public double valueOf() {
        return coeff.valueOf() * Math.sqrt(arg.valueOf());
    }
    @Override
    public RealNumber flipSign() {
        return new SquareRoot(coeff.flipSign(), arg);
    }

    @Override
    public RealNumber multiply(RealNumber n) {
        if(!(n instanceof SquareRoot)) {
            return new RealDouble(valueOf() * n.valueOf());
        }
        SquareRoot sqrt = (SquareRoot)n;
        RealNumber newCoeff = coeff.multiply(sqrt.coeff);
        RealNumber newArg = arg.multiply(sqrt.arg);
        if(newArg.valueOf() == Math.round(newArg.valueOf()) && Math.sqrt(newArg.valueOf()) == Math.round(Math.sqrt(newArg.valueOf()))) {
            return newCoeff.multiply(new RealDouble(Math.sqrt(newArg.valueOf())));
        }else {
            return new SquareRoot(newCoeff, newArg);
        }
    }

    @Override
    public String toString() {
        String addCo = coeff.toString();
        if(coeff.valueOf() == 1.0) addCo = "";
        if(coeff.valueOf() == -1.0) addCo = "-";
        return addCo + "sqrt" + arg; 
    }
    /**
     * Returns the string of the absolute value of this number
     */
    public String absoluteString() {
        String addCo = "";
        if(coeff.valueOf() != 1.0 && coeff.valueOf() != -1) {
            addCo = coeff.absoluteString();
        }
        return addCo + "sqrt" + arg;
    }
}
