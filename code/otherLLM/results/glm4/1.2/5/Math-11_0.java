import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class MultivariateNormalDistribution {

    private double[] mean;
    private double[][] covarianceMatrix;
    private final double covarianceMatrixDeterminant;
    private final double covarianceMatrixInverseDeterminant;
    private final double[] covarianceMatrixInverse;

    public MultivariateNormalDistribution(double[] mean, double[][] covarianceMatrix) {
        this.mean = mean;
        this.covarianceMatrix = covarianceMatrix;
        int dim = mean.length;

        // Compute determinant of the covariance matrix
        this.covarianceMatrixDeterminant = Math.abs(new LUDecomposition(covarianceMatrix).getDeterminant());

        // Compute inverse determinant
        this.covarianceMatrixInverseDeterminant = 1.0 / covarianceMatrixDeterminant;

        // Compute inverse of the covariance matrix
        this.covarianceMatrixInverse = new double[dim * dim];
        LUDecomposition luDecom = new LUDecomposition(covarianceMatrix);
        luDecom.getLU(this.covarianceMatrixInverse);

        // Check for singularity and set to identity if necessary
        if (covarianceMatrixDeterminant == 0) {
            this.covarianceMatrixInverse = IdentityMatrix.create(dim, dim);
        }
    }

    public double density(final double[] vals) throws DimensionMismatchException {
        final int dim = mean.length;
        if (vals.length != dim) {
            throw new DimensionMismatchException(vals.length, dim);
        }

        // Compute the exponent term for multivariate normal distribution
        double exponentTerm = 0.5 * Math.log(covarianceMatrixDeterminant);

        for (int i = 0; i < dim; i++) {
            exponentTerm -= 0.5 * (vals[i] - mean[i]) * covarianceMatrixInverse[i][i];
        }

        return FastMath.pow(2 * FastMath.PI, -dim / 2) *
               Math.pow(covarianceMatrixDeterminant, -0.5) *
               Math.exp(exponentTerm);
    }

    private double getExponentTerm(double[] vals) {
        // This method is not used in the fixed version and can be removed or repurposed if needed.
        return 0;
    }
}