import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;

public class DensityCalculator {
    private int dimension; // Assuming dimension is an instance variable
    private double covarianceMatrixDeterminant; // Assuming this is an instance variable

    public DensityCalculator(int dimension, double covarianceMatrixDeterminant) {
        this.dimension = dimension;
        this.covarianceMatrixDeterminant = covarianceMatrixDeterminant;
    }

    public int getDimension() {
        return dimension;
    }

    public double getExponentTerm(double[] vals) {
        // This method should be implemented to calculate the exponent term.
        // Placeholder return statement:
        return FastMath.exp(-0.5); // Example placeholder
    }

    public double density(final double[] vals) throws DimensionMismatchException {
        final int dim = getDimension();
        if (vals.length != dim) {
            throw new DimensionMismatchException(vals.length, dim);
        }

        return FastMath.pow(2 * FastMath.PI, -dim / 2.0) *
            FastMath.pow(covarianceMatrixDeterminant, -0.5) *
            getExponentTerm(vals);
    }
}