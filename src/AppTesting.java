
// This broke when porting over to javafx, idk how to fix it, you may need to bring in junit testing code somehow
// probably have to add junit library or something


//import static org.junit.Assert.assertTrue;

//import org.junit.Test;

public class AppTesting {
    /*
    @Test
    public void evaluateExpressionTest() {
        ComplexNumber num1 = ComplexNumber.parseComplexNumber("5-2i");
        ComplexNumber num2 = ComplexNumber.parseComplexNumber("2+5i");
        ComplexNumber num3 = ComplexNumber.parseComplexNumber("9-9i");
        ComplexNumber eval = ComplexCalculator.add(ComplexNumber.multiply(num1, num2), num3);
        assertTrue(eval.equals(ComplexCalculator.evaluateExpression("(5-2i)*(2+5i)+(9-9i)")));
    }

    @Test
    public void multiplicationInterpretationsTest() {
        ComplexNumber eval1 = ComplexCalculator.evaluateExpression("(8-2i)*(1+7i)");
        ComplexNumber eval2 = ComplexCalculator.evaluateExpression("(8-2i)(1+7i)");
        assertTrue(eval1.equals(eval2));
    }

    @Test 
    public void parenthesisTest() {
        ComplexNumber eval1 = ComplexCalculator.evaluateExpression("(5-2i) + (-3-3i)");
        eval1 = ComplexCalculator.subtract(eval1, ComplexNumber.parseComplexNumber("4+4i"));
        String expression = "((5-2i)+(-3-3i)) - (4+4i)";
        assertTrue(eval1.equals(ComplexCalculator.evaluateExpression(expression)));
    }

    @Test
    public void identityTest() {
        String[][] testMatrix = {
            {"8-2i", "3+i"},
            {"-2-i", "5-6i"}
        };
        ComplexMatrix m = new ComplexMatrix(testMatrix);
        ComplexMatrix i = ComplexMatrix.findIdentityMatrix(2);
        assertTrue(ComplexMatrix.matrixMultiply(m, i).equals(m));
    }
*/
}
