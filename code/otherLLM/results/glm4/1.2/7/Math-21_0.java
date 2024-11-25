import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

public class RectangularCholeskyDecomposition {

    private RealMatrix root;

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small) throws NonPositiveDefiniteMatrixException {
        final int order = matrix.getRowDimension();
        final double[][] c = matrix.getData();

        if (order != matrix.getColumnDimension()) {
            throw new IllegalArgumentException("The input matrix must be square.");
        }

        // Verify that the matrix is symmetric
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j <= i; ++j) {
                if (c[i][j] != c[j][i]) {
                    throw new NonPositiveDefiniteMatrixException("The matrix is not symmetric.");
                }
            }
        }

        // Initialize arrays
        int[] permute = new int[order];
        boolean[] selected = new boolean[order];

        for (int i = 0; i < order; ++i) {
            permute[i] = i;
        }

        // Perform Cholesky decomposition
        for (int k = 0; k < order; ++k) {
            if (selected[k]) continue;

            int m = k;
            double maxRatio = -1.0;
            for (int i = k; i < order; ++i) {
                double ratio = c[permute[i]][permute[k]] / Math.sqrt(c[permute[i]][permute[i]]);
                if (ratio > maxRatio) {
                    maxRatio = ratio;
                    m = i;
                }
            }

            // Swap rows to make the diagonal element the largest
            int temp = permute[m];
            permute[m] = permute[k];
            permute[k] = temp;

            selected[k] = true;

            for (int i = k + 1; i < order; ++i) {
                double ratio = c[permute[i]][permute[k]] / Math.sqrt(c[permute[k]][permute[k]]);
                c[permute[i]][permute[k]] = 0.0;
                c[permute[k]][permute[i]] = 0.0;

                for (int j = k + 1; j < order; ++j) {
                    double element = c[permute[i]][permute[j]] - ratio * c[permute[k]][permute[j]];
                    c[permute[i]][permute[j]] = element;
                    c[permute[j]][permute[i]] = element;
                }
            }

            // Check for non-positive definiteness
            double d = Math.sqrt(c[permute[k]][permute[k]]);
            if (d < small) {
                throw new NonPositiveDefiniteMatrixException(d, k, small);
            }
        }

        // Build the root matrix using permutation
        RealMatrix permutedMatrix = MatrixUtils.createRealMatrix(order, order);
        for (int i = 0; i < order; ++i) {
            permutedMatrix.setRow(i, c[permute[i]]);
        }
        this.root = permutedMatrix;

        // Set the rank of the matrix
        int rank = 0;
        for (double value : MathUtils.toPrimitiveArray(this.root.getColumn(0))) {
            if (Math.abs(value) > small) {
                ++rank;
            } else {
                break;
            }
        }
    }

    public RealMatrix getRootMatrix() {
        return root;
    }
}