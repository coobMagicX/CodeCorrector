import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

private RealMatrix squareRoot(RealMatrix m) {
    // Check if matrix is a square matrix
    int rows = m.getRowDimension();
    int cols = m.getColumnDimension();

    if (rows != cols) {
        throw new IllegalArgumentException("The input matrix must be square.");
    }

    try {
        final EigenDecomposition dec = new EigenDecomposition(m);
        return dec.getSquareRoot();
    } catch (IllegalArgumentException e) {
        // This exception can occur if the matrix is singular or not positive definite
        throw new ArithmeticException("Cannot compute square root of a non-positive definite matrix.");
    }
}