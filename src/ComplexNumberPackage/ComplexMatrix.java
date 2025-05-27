package ComplexNumberPackage;
import RealNumberPackage.RealDouble;

@FunctionalInterface
interface StringOp {
    String perform(ComplexNumber n);
}

public class ComplexMatrix {
    
    private ComplexNumber[][] matrix;

    public ComplexMatrix(ComplexNumber[][] matrix) {
        this.matrix = new ComplexNumber[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int k = 0; k < matrix[0].length; k++) {
                this.matrix[i][k] = matrix[i][k].clone();
            }
        }
    }
    public ComplexMatrix(String[][] s) {
        matrix = new ComplexNumber[s.length][s[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int k = 0; k < matrix[0].length; k++) {
                matrix[i][k] = ComplexNumber.parseComplexNumber(s[i][k]);
            }
        }
    }

    public ComplexMatrix(ComplexNumber[] vector) {
        matrix = new ComplexNumber[vector.length][1];
        for(int i = 0; i < vector.length; i++) {
            matrix[i][0] = vector[i].clone();
        }
    }
    public ComplexMatrix(String[] v) {
        matrix = new ComplexNumber[v.length][1];
        for(int i = 0; i < v.length; i++) {
            matrix[i][0] = ComplexNumber.parseComplexNumber(v[i]);
        }
    }

    public ComplexNumber[][] getMatrix() {
        return matrix;
    }
    /**Returns a 2D array that is a copy of the matrix array of this object */
    public ComplexNumber[][] getCopyOfMatrixArray() {
        ComplexNumber[][] m = new ComplexNumber[matrix.length][matrix[0].length];
        for(int i = 0; i < m.length; i++) {
            for(int k = 0; k < m[0].length; k++) {
                m[i][k] = matrix[i][k].clone();
            }
        }
        return m;
    }

    public ComplexMatrix findZero() {
        ComplexNumber[][] ar = new ComplexNumber[matrix.length][matrix[0].length];
        for(int i = 0; i < ar.length; i++) {
            for(int k = 0; k < ar[0].length; k++) {
                ar[i][k] = new ComplexNumber(new RealDouble(), new RealDouble());
            }
        }
        return new ComplexMatrix(ar);
    }

    @Override
    public String toString() {
        return buildStringWithOp((cn) -> cn.toString());
    }
    public String toString(boolean hideImaginaryPart) {
        if(!hideImaginaryPart) return toString();
        return buildStringWithOp((cn) -> cn.toStringRealOnly());
    }
    private String buildStringWithOp(StringOp sop) {
        String s = "\n";
        for(int i = 0; i < matrix.length; i++) {
            s += "[";
            for(int k = 0; k < matrix[0].length; k++) {
                s += sop.perform(matrix[i][k]);
                if(k < matrix[0].length-1) {
                    s += ", ";
                }
            }
            s += "]\n";
        }
        return s;
    }
    public ComplexMatrix clone() {
        return new ComplexMatrix(getCopyOfMatrixArray());
    }
    public boolean equals(Object o) {
        if(!(o instanceof ComplexMatrix)) {return false;}
        ComplexMatrix m = (ComplexMatrix)o;
        if(m.getMatrix().length != matrix.length || m.getMatrix()[0].length != matrix[0].length) {return false;}
        for(int i = 0; i < matrix.length; ++i) {
            for(int k = 0; k < matrix[0].length; ++k) {
                if(!matrix[i][k].equals(m.getMatrix()[i][k])) {return false;}
            }
        }
        return true;
    }

    private static void exitIfNotVector(ComplexMatrix v1, ComplexMatrix v2) throws Exception {
        if((v1.matrix[0].length > 1 && v1.matrix.length > 1) || (v2.matrix[0].length > 1 && v2.matrix.length > 1)) {
            throw new Exception("Input was not a vector, cannot perform calculation!");
        }
    }
    private static void exitIfNotSquareMatrix(ComplexMatrix a) throws Exception {
        if(a.matrix.length != a.matrix[0].length) {
            throw new Exception("Inputted matrix is not a square matrix, cannot perform calculation!");
        }
    }

    /**Returns a ComplexVectorSpace representing v + w */
    public static ComplexMatrix add(ComplexMatrix v, ComplexMatrix w) throws Exception {
        if(v.getMatrix().length != w.getMatrix().length || v.matrix[0].length != w.matrix[0].length) {throw new Exception("Matricies are not the same size, cannot add!");}
        ComplexNumber[][] newAr = new ComplexNumber[v.getMatrix().length][v.getMatrix()[0].length];
        for(int i = 0; i < newAr.length; i++) {
            for(int k = 0; k < newAr[0].length; k++) {
                newAr[i][k] = ComplexCalculator.add(v.getMatrix()[i][k], w.getMatrix()[i][k]);
            }
        }
        return new ComplexMatrix(newAr);
    }
    /**Returns a ComplexVectorSpace representing v - w */
    public static ComplexMatrix subtract(ComplexMatrix v, ComplexMatrix w) throws Exception {
        if(v.getMatrix().length != w.getMatrix().length || v.matrix[0].length != w.matrix[0].length) {throw new Exception("Matricies are not the same size, cannot subtract!");}
        ComplexNumber[][] newAr = new ComplexNumber[v.getMatrix().length][v.getMatrix()[0].length];
        for(int i = 0; i < newAr.length; i++) {
            for(int k = 0; k < newAr[0].length; k++) {
                newAr[i][k] = ComplexCalculator.subtract(v.getMatrix()[i][k], w.getMatrix()[i][k]);
            }
        }
        return new ComplexMatrix(newAr);
    }
    /**Returns the negation of v, which flips the signs of all of the numbers */
    public static ComplexMatrix negate(ComplexMatrix v) {
        ComplexNumber[][] newAr = new ComplexNumber[v.getMatrix().length][v.getMatrix()[0].length];
        for(int i = 0; i < newAr.length; i++) {
            for(int k = 0; k < newAr[0].length; k++) {
                ComplexNumber numAt = v.getMatrix()[i][k];
                newAr[i][k] = new ComplexNumber(numAt.realPart().flipSign(), numAt.imaginaryPart().flipSign());
            }
        }
        return new ComplexMatrix(newAr);
    }
    /**Returns the result of v * c, where c is a ComplexNumber */
    public static ComplexMatrix scalarMultiply(ComplexMatrix v, ComplexNumber c) {
        ComplexNumber[][] newAr = new ComplexNumber[v.getMatrix().length][v.getMatrix()[0].length];
        for(int i = 0; i < newAr.length; i++) {
            for(int k = 0; k < newAr[0].length; k++) {
                newAr[i][k] = ComplexCalculator.multiply(v.getMatrix()[i][k], c);
            }
        }
        return new ComplexMatrix(newAr);
    }
    /**Returns the conjugate of matrix v, which is defined as finding the conjugate of all of the numbers in the matrix */
    public static ComplexMatrix conjugate(ComplexMatrix v) {
        ComplexNumber[][] m = v.getCopyOfMatrixArray();
        for(int i = 0; i < m.length; i++) {
            for(int k = 0; k < m[0].length; k++) {
                m[i][k] = ComplexNumber.conjugate(m[i][k]);
            }
        }
        return new ComplexMatrix(m);
    }

    /**Returns the transpose of the matrix, which is defined as the following (A[j][k])T = A[k][j] */
    public static ComplexMatrix transpose(ComplexMatrix v) {
        ComplexNumber[][] m = new ComplexNumber[v.getMatrix()[0].length][v.getMatrix().length];
        for(int i = 0; i < v.getMatrix().length; i++) {
            for(int k = 0; k < v.getMatrix()[0].length; k++) {
                m[k][i] = v.getMatrix()[i][k];
            }
        }
        return new ComplexMatrix(m);
    }
    /**Returns the adjoint of the matrix, which is defined as doing the conjugate and transpose of the matrix. Also known as the dagger operation. */
    public static ComplexMatrix adjoint(ComplexMatrix v) {
        ComplexMatrix cm = v.clone();
        cm = ComplexMatrix.conjugate(cm);
        return ComplexMatrix.transpose(cm);
    }

    /**Returns the result of A * B, where * denotes matrix multiplication. Matrix multiplication is defined as: 
     * given a is in C^(m*n) and B is in C^(n*p), (A*B)[j,k] = Σ(bound: n-1)(h=0)(A[j,h]*B[h,k])
     */
    public static ComplexMatrix matrixMultiply(ComplexMatrix a, ComplexMatrix b) throws Exception {
        //number of columns in a must equal number of rows in b in order for this operation to work
        if(a.getMatrix()[0].length != b.getMatrix().length) {throw new Exception("Cannot perform matrix multiplicatino on " + a + " and " + b);}

        ComplexNumber[][] m = new ComplexNumber[a.getMatrix().length][b.getMatrix()[0].length];

        for(int i = 0; i < m.length; ++i) {
            for(int k = 0; k < m[0].length; k++) {
                ComplexNumber total = new ComplexNumber();
                for(int j = 0; j < a.getMatrix()[0].length; ++j) {
                    total = ComplexCalculator.add(total, ComplexCalculator.multiply(a.getMatrix()[i][j], b.getMatrix()[j][k]));
                }
                m[i][k] = total;
            }
        }

        return new ComplexMatrix(m);
    }
    /**Returns the identity matrix of an n by n grid, which is the unit of multiplication. Follows property: A * I = A = I * A */
    public static ComplexMatrix findIdentityMatrix(int dim) {
        ComplexNumber[][] m = new ComplexNumber[dim][dim];
        for(int i = 0; i < dim; i++) {
            for(int k = 0; k < dim; k++) {
                ComplexNumber n = new ComplexNumber();
                if(i == k) {n = new ComplexNumber(new RealDouble(1),new RealDouble());}
                m[i][k] = n;
            }
        }
        return new ComplexMatrix(m);
    }
    /**
     * Returns the trace of m, which is the sum of all diagonal elements of m
     * @param m - a square matrix
     */
    public static ComplexNumber trace(ComplexMatrix m) throws Exception {
        if(m.matrix.length != m.matrix[0].length) {
            throw new Exception("Inputted matrix is not a square matrix!");
        }
        ComplexNumber sum = new ComplexNumber();
        for(int i = 0; i < m.matrix.length; i++) {
            sum = ComplexCalculator.add(sum, m.matrix[i][i]);
        }
        return sum;
    }
    
    /**
     * Finds the inner product of the two vectors/matrices
     * Both inputs (m1, m2) need to be in the same vector space in order for this to work, as the methods for finding inner products
     * can change between complex vector spaces.
     * 
     * Current Inner Products this can solve:
     *  - Two elements of C^N
     *  - Two elements of C^(NxN)
     */
    public static ComplexNumber innerProduct(ComplexMatrix m1, ComplexMatrix m2) throws Exception {
        if(m1.matrix.length != m2.matrix.length || m1.matrix[0].length != m2.matrix[0].length || 
            (m1.matrix[0].length > 1 && (m1.matrix.length != m1.matrix[0].length || m2.matrix.length != m2.matrix[0].length))) {
            throw new Exception("Inputted matricies not valid for inner products!");
        }

        if(m1.matrix[0].length == 1) {
            return innerProductCN(m1, m2);
        }else {
            return innerProductCNxN(m1, m2);
        }
    }
    private static ComplexNumber innerProductCN(ComplexMatrix v1, ComplexMatrix v2) throws Exception {
        ComplexMatrix vD = ComplexMatrix.adjoint(v1);
        return ComplexMatrix.matrixMultiply(vD, v2).matrix[0][0]; // should only have one element, which is our answer
    }
    private static ComplexNumber innerProductCNxN(ComplexMatrix a, ComplexMatrix b) throws Exception {
        ComplexMatrix alt = ComplexMatrix.adjoint(a);
        ComplexMatrix sum = ComplexMatrix.matrixMultiply(alt, b);
        return ComplexMatrix.trace(sum);
    }

    /**
     * Returns the norm of the given vector, note this does not work with matricies. The answer is the sqrt of the inner product
     * of the two vectors.
     */
    public static double norm(ComplexMatrix v) throws Exception {
        if(v.matrix[0].length > 1 && v.matrix.length > 1) {
            throw new Exception("Inputted matrix was not a vector!");
        }
        ComplexNumber result = innerProduct(v, v);
        if(result.imaginaryPart().valueOf() != 0.0) {
            throw new Exception("Result of norm was not real!");
        }
        return Math.sqrt(result.realPart().valueOf());
    }
    /**
     * Returns the distance between two vectors, aka the norm of v1 - v2
     */
    public static double distance(ComplexMatrix v1, ComplexMatrix v2) throws Exception {
        exitIfNotVector(v1, v2);
        return norm(ComplexMatrix.subtract(v1, v2));
    }

    /**
     * Calculates the angle between the two vectors. The inner product of the two vectors must produce a real number
     * or this method will fail.
     */
    public static double angleBetweenVector(ComplexMatrix v1, ComplexMatrix v2) throws Exception {
        exitIfNotVector(v1, v2);
        ComplexNumber temp = ComplexCalculator.divide(innerProduct(v1, v2), new ComplexNumber(new RealDouble(norm(v1) * norm(v2)), new RealDouble()));
        if(temp.imaginaryPart().valueOf() != 0.0) {
            System.out.println("Angle calculation has failed as the inner product is not real!");
        }
        return Math.acos(temp.realPart().valueOf());
    }

    /**
     * Determines whether or not a given square matrix is hermitian, meaning that the adjoint/dagger of the matrix is equivalent
     * to the matrix itself.
     */
    public static boolean isHermitian(ComplexMatrix a) throws Exception {
        exitIfNotSquareMatrix(a);
        ComplexMatrix adjoint = adjoint(a);
        return a.equals(adjoint);
    }

    /**
     * Determines whether or not a given square matrix is unitary, meaning it multiplied its adjoint gives the identity matrix.
     */
    public static boolean isUnitary(ComplexMatrix a) throws Exception {
        exitIfNotSquareMatrix(a);
        ComplexMatrix i = findIdentityMatrix(a.matrix.length);
        return ComplexMatrix.matrixMultiply(ComplexMatrix.adjoint(a.clone()), a).equals(i);
    }

    /**
     * Returns the tensor product of a and b. Accepts any kind of matrix (vectors included!)
     */
    public static ComplexMatrix tensorProduct(ComplexMatrix a, ComplexMatrix b) {
        ComplexNumber[][] matrix = new ComplexNumber[a.matrix.length * b.matrix.length][a.matrix[0].length * b.matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int k = 0; k < matrix[0].length; k++) {
                matrix[i][k] = ComplexCalculator.multiply(a.matrix[i/b.matrix.length][k/b.matrix[0].length], b.matrix[i % b.matrix.length][k % b.matrix[0].length]);
            }
        }
        return new ComplexMatrix(matrix);
    }

    /**
     * Returns the product obtained through boolean matrix multiplication. Currently designed to only work with square matricies
     * describing the dynamics of a system. Note that all numbers in the matrix should be real, and either 0 or 1
     */
    public static ComplexMatrix booleanMatrixMultiplication(ComplexMatrix a, ComplexMatrix b) throws Exception {
        exitIfNotSquareMatrix(a);
        exitIfNotSquareMatrix(b);
        ComplexNumber[][] matrix = new ComplexNumber[a.matrix.length][a.matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                boolean state = false;
                for(int k = 0; k < matrix.length; k++) {
                    if(a.matrix[i][k].realPart().valueOf() == 1.0 && b.matrix[k][j].realPart().valueOf() == 1.0) {
                        state = true;
                        break;
                    }
                }
                int real = state ? 1 : 0;
                matrix[i][j] = new ComplexNumber(new RealDouble(real), new RealDouble());
            }
        }
        return new ComplexMatrix(matrix);
    }

    /**
     * Evaluates the probability of a certain position being measured in a quantum state. Input must be a vector. Calculation is as follows:
     * |v[dex]|^2 / |v|^2
     */
    public static double evaluateProbability(ComplexMatrix v, int dex) throws Exception {
        exitIfNotVector(v, v);
        double vNorm = ComplexMatrix.norm(v);
        double nNorm = ComplexNumber.modulusSquare(v.getMatrix()[dex][0]);
        return nNorm / (vNorm * vNorm);
    }

    /**
     * Normalizes the vector to one which has a norm, i.e. length, of 1.
     */
    public static ComplexMatrix normalizeVector(ComplexMatrix v) throws Exception {
        exitIfNotVector(v, v);
        ComplexNumber n = new ComplexNumber(new RealDouble(ComplexMatrix.norm(v)),new RealDouble());
        ComplexNumber[][] m = new ComplexNumber[v.matrix.length][1];
        for(int i = 0; i < m.length; i++) {
            m[i][0] = ComplexNumber.divide(v.matrix[i][0],n);
        }
        return new ComplexMatrix(m);
    }

    /**
     * Calculatres the commutator of a and b, which if it is the zero operator, then the product of the two hermitian matricies is hermitian.
     */
    public static ComplexMatrix commutator(ComplexMatrix a, ComplexMatrix b) throws Exception {
        exitIfNotSquareMatrix(a);
        exitIfNotSquareMatrix(b);
        return ComplexMatrix.subtract(ComplexMatrix.matrixMultiply(a, b), ComplexMatrix.matrixMultiply(b, a));
    }

    /**
     * Finds the expected value of a (hermitian) operator on a state
     */
    public static ComplexNumber expectedValue(ComplexMatrix a, ComplexMatrix v) throws Exception {
        exitIfNotSquareMatrix(a);
        exitIfNotVector(v, v);
        ComplexMatrix bra = ComplexMatrix.matrixMultiply(a, v);
        return innerProductCN(bra, v);
    }

    /**
     * Calculates the demeaned version of a square hermitian operation on a given state.
     */
    public static ComplexMatrix demean(ComplexMatrix a, ComplexMatrix v) throws Exception {
        exitIfNotSquareMatrix(a);
        ComplexMatrix I = findIdentityMatrix(a.matrix.length);
        I = ComplexMatrix.scalarMultiply(I, expectedValue(a, v));
        return ComplexMatrix.subtract(a, I);
    }

    /**
     * Calculates the variance of a given operator on a state.
     */
    public static ComplexNumber variance(ComplexMatrix a, ComplexMatrix v) throws Exception {
        ComplexMatrix demean = demean(a, v);
        demean = ComplexMatrix.matrixMultiply(demean, demean);
        return expectedValue(demean, v);
    }
}
