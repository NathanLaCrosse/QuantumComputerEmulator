package RealNumberPackage;
/**
 * This class represents a double and is in the RealNumber structure
 */
public class RealDouble extends RealNumber {
    private double num;

    public RealDouble(double num) {
        this.num = num;
    }
    public RealDouble() {
        num = 0;
    }

    @Override 
    public double valueOf() {
        return num;
    }
    @Override
    public RealNumber flipSign() {
        return new RealDouble(-num);
    }
}
