import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecomposition {
    private RealMatrix root;
    private int rank;

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
        throws NonPositiveDefiniteMatrixException {

        final int order = matrix.getRowDimension();
        final double[][] c = matrix.getData();
        final double[][] b = new double[order][order];

        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        int r = 0;
        for (boolean loop = true; loop;) {
            // find maximal diagonal element
            int swap = r;
            for (int i = r + 1; i < order; ++i) {
                if (c[index[i]][index[i]] > c[index[swap]][index[swap]]) {
                    swap = i;
                }
            }

            // swap elements
            if (swap != r) {
                int tmp = index[r];
                index[r] = index[swap];
                index[swap] = tmp;
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
                loop = false;
            } else {
                // transform the matrix
                final double sqrt = FastMath.sqrt(c[ir][ir]);
                b[r][r] = sqrt;
                final double inverse = 1 / sqrt;
                for (int i = r + 1; i < order; ++i) {
                    final int ii = index[i];
                    final double e = inverse * c[ii][ir];
                    b[i][r] = e;
                    c[ii][ii] -= e * e;
                    for (int j = r + 1; j < i; ++j) {
                        final int ij = index[j];
                        final double f = c[ii][ij] - e * b[j][r];
                        c[ii][ij] = f;
                        c[ij][ii] = f;
                    }
                }

                // prepare next iteration
                loop = ++r < order;
            }
        }

        // build the root matrix
        rank = r;
        root = MatrixUtils.createRealMatrix(order, r);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < r; ++j) {
                root.setEntry(index[i], j, b[i][j]);
            }
        }
    }

    public RealMatrix getRootMatrix() {
        return root;
    }
}