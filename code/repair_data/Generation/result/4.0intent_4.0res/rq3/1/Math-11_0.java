import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;

public class DensityCalculator {
    private double covarianceMatrixDeterminant;
    private int dimension;

    public DensityCalculator(double covarianceMatrixDeterminant, int dimension) {
        this.covarianceMatrixDeterminant = covarianceMatrixDeterminant;
        this.dimension = dimension;
    }

    public int getDimension() {
        return this.dimension;
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

    // Example implementation of getExponentTerm
    private double getExponentTerm(double[] vals) {
        // This should be replaced with the actual calculation of the exponent term
        return FastMath.exp(-0.5 * Math.random()); // Placeholder: Random exponent term
    }
}