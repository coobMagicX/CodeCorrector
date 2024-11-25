import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecomposition implements Decomposition {
    private RealMatrix root;
    private int rank;

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
            throws NonPositiveDefiniteMatrixException {

        final int order = matrix.getRowDimension();
        final double[][] c = matrix.getData();
        final double[][] b = new double[order][order];

        RealMatrix realMatrix = new Array2DRowRealMatrix(c);
        if (!realMatrix.isSymmetric()) {
            throw new NonPositiveDefiniteMatrixException("The matrix is not symmetric.");
        }

        int[] swap = new int[order];
        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        int r = 0;
        while (r < order) {

            // find maximal diagonal element
            swap[r] = r;
            for (int i = r + 1; i < order; ++i) {
                int ii = index[i];
                int isi = index[swap[i]];
                if (c[ii][ii] > c[isi][isi]) {
                    swap[r] = i;
                }
            }

            // swap elements
            if (swap[r] != r) {
                int tmp = index[r];
                index[r] = index[swap[r]];
                index[swap[r]] = tmp;
            }

            // check diagonal element
            int ir = index[r];
            if (c[ir][ir] < small) {

                if (r == 0) {
                    throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
                }

                // check remaining diagonal elements
                for (int i = r; i < order; ++i) {
                    if (c[index[i]][index[i]] < -small) {
                        throw new NonPositiveDefiniteMatrixException(c[index[i]][index[i]], i, small);
                    }
                }

                ++r;
            } else {

                // transform the matrix
                double sqrt = FastMath.sqrt(c[ir][ir]);
                b[r][r] = sqrt;
                for (int i = r + 1; i < order; ++i) {
                    final int ii = index[i];
                    double e = c[ii][ir] / sqrt;
                    b[i][r] = e;
                    for (int j = r; j < i; ++j) {
                        final int jj = index[j];
                        double f = c[ii][jj] - e * b[j][r];
                        c[ii][jj] = f;
                        c[jj][ii] = f;
                    }
                }

                // update the original matrix
                for (int i = r + 1; i < order; ++i) {
                    final int ii = index[i];
                    double e = c[i][ir] / sqrt;
                    b[i][r] = e;
                    for (int j = r + 1; j < order; ++j) {
                        final int jj = index[j];
                        double f = c[i][jj] - e * b[j][r];
                        c[i][jj] = f;
                        c[jj][i] = f;
                    }
                }

                // prepare next iteration
                r++;
            }
        }

        // build the root matrix
        rank = r;
        RealMatrix tempRoot = new Array2DRowRealMatrix(order, r);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < r; ++j) {
                tempRoot.setEntry(i, j, b[index[i]][j]);
            }
        }

        // Adjust the root matrix to have only lower triangular elements
        this.root = tempRoot.subMatrix(0, order - 1, 0, r);
    }

    public RealMatrix getRootMatrix() {
        return root;
    }
}