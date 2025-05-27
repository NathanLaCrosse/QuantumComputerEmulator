package ComplexNumberPackage;
import java.util.ArrayList;

/**
 * This class is a surface class meant to utilize the combined powers of complex numbers and
 * their polar coordinate counterparts.
 * There are a few redundancies in this class where we just use class methods from the other classes,
 * but I feel its necessary to have all operations in one place.
 * All methods in this class return numbers in the ComplexNumber format, not PolarCoordinate.
 */
public class ComplexCalculator {
    //These methods help convert between two interpretations of complex numbers, and both are very
    //valuable when solving problems
    public static PolarCoordinate asPolarCoordinate(ComplexNumber c) {
        return new PolarCoordinate(c);
    }
    public static ComplexNumber asComplexNumber(PolarCoordinate p) {
        return new ComplexNumber(p);
    }

    //The four main operations (+ conjugate and modulus) are easily done in the ComplexNumber class, so
    //we just use them here

    /**Returns a+b */
    public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return ComplexNumber.add(a, b);
    }
    /**Returns a-b */
    public static ComplexNumber subtract(ComplexNumber a, ComplexNumber b) {
        return ComplexNumber.subtract(a, b);
    }
    /**Returns a*b */
    public static ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        return ComplexNumber.multiply(a, b);
    }
    /**Returns a/b */
    public static ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        return ComplexNumber.divide(a, b);
    }
    /**Returns the conjugate of a, which is defined as flipping the imaginary part of the complex number*/
    public static ComplexNumber conjugate(ComplexNumber a) {
        return ComplexNumber.conjugate(a);
    }
    /**Returns the modulus of the ComplexNumber aka the hypotenuse of the triangle it makes */
    public static double modulus(ComplexNumber a) {
        return ComplexNumber.modulus(a);
    }

    //Powers and roots are a bit more difficult, so we rely on polar coordinates to help, as there 
    //are much easier formulas in this interpretation

    /**Returns the ComplexNumber to the nth power */
    public static ComplexNumber pow(ComplexNumber a, int n) {
        return asComplexNumber(PolarCoordinate.raiseToPower(asPolarCoordinate(a), n));
    }
    /**Returns n nth roots of the ComplexNumber */
    public static ComplexNumber[] roots(ComplexNumber a, int n) {
        PolarCoordinate[] pRoots = PolarCoordinate.findRoots(asPolarCoordinate(a), n);
        ComplexNumber[] roots = new ComplexNumber[pRoots.length];
        for(int i = 0; i < pRoots.length; i++) {
            roots[i] = asComplexNumber(pRoots[i]);
        }
        return roots;
    }

    /**Returns a complex number that is the answer of the given expression. Currently works with addition, subtraction, multiplication and division.
     * Complex numbers must be denoted using parenthesis.
     */
    public static ComplexNumber evaluateExpression(String s) {
        s.trim();
        ArrayList<ComplexNumber> vals = new ArrayList<>();
        ArrayList<Character> operations = new ArrayList<>();

        //check to see if we have an inner expression (parenthesis)
        int begin = s.indexOf("(");
        if(s.indexOf("(", begin + 1) < s.indexOf(")", begin + 1)) {
            //we have found our starting place, where there is at least a double parenthesis
            int secondParenLoc = s.indexOf("(", begin + 1);
            ArrayList<Integer> beginParenLoc = new ArrayList<>();
            beginParenLoc.add(begin);
            beginParenLoc.add(secondParenLoc);
            int i;
            for(i = secondParenLoc + 1; beginParenLoc.size() > 0; i++) {
                if(s.charAt(i) == '(') {
                    beginParenLoc.add(i);
                }else if(s.charAt(i) == ')') {
                    beginParenLoc.remove(beginParenLoc.size() - 1);
                }
            }
            String innerExpression = s.substring(begin + 1, i - 1); // i denotes 1 after end of expression
            //evaluate the inner expression
            ComplexNumber eval = evaluateExpression(innerExpression);
            //modifiy our starting string to include new number, and then evaluate that expression
            String adaptedString = s.substring(0, begin) + eval + s.substring(i +1);
            return evaluateExpression(adaptedString);
        }

        while(s.indexOf("(") != -1) {
            //complex nums will be in parenthesis, so find them
            int start = s.indexOf("(");
            int end = s.indexOf(")");

            //see if we have touching parenthesis (implying multiplication)
            String extraMult = (end != s.length() - 1 && s.charAt(end + 1) == '(') ? "*" : "";

            //remove complex number from string
            String cutOut = s.substring(start + 1, end);
            vals.add(ComplexNumber.parseComplexNumber(cutOut));

            //change string to reflect cutOut, careful to remove parenthesis
            //also adds in an extra multiplication symbol if we need it
            s = s.substring(0, start) + extraMult + s.substring(end + 1);
        }
        //record operands in remaining list
        for(int i = 0; i < s.length(); i++) {
            if(isOperation(s.charAt(i))) {
                operations.add(s.charAt(i));
            }
        }
        //an important thing to notice for the following code: there will also be more operands
        //than terms, so we make use of this
        for(int i = 0; i < operations.size(); i++) {
            ComplexNumber result = null;
            boolean operationPreformed = true;
            if(operations.get(i) == '*') {
                result = ComplexNumber.multiply(vals.get(i), vals.get(i+1));
            }else if(operations.get(i) == '/') {
                result = ComplexNumber.divide(vals.get(i), vals.get(i+1));
            }else {
                operationPreformed = false;
            }
            if(operationPreformed) {
                //get rid of old numbers
                vals.remove(i);
                vals.remove(i);
                //add in new number
                vals.add(i, result);
                //remove operation from list
                operations.remove(i);
                i--;
            }
        }
        //order of operations demands we do another for loop
        for(int i = 0; i < operations.size(); i++) {
            ComplexNumber result = null;
            boolean operationPreformed = true;
            if(operations.get(i) == '+') {
                result = ComplexNumber.add(vals.get(i), vals.get(i+1));
            }else if(operations.get(i) == '-') {
                result = ComplexNumber.subtract(vals.get(i), vals.get(i+1));
            }else {
                operationPreformed = false;
            }
            if(operationPreformed) {
                //get rid of old numbers
                vals.remove(i);
                vals.remove(i);
                //add in new number
                vals.add(i, result);
                //remove operation from list
                operations.remove(i);
                i--;
            }
        }
        return vals.get(0);
    }
    private static boolean isOperation(char c) {
        return isAddSubtract(c) || isMultiplyDivide(c);
    }
    private static boolean isAddSubtract(char c) {
        return c == '+' || c == '-';
    }
    private static boolean isMultiplyDivide(char c) {
        return c == '*' || c == '/';
    }
}
