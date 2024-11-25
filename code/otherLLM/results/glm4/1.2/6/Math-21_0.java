import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecomposition {

    private int rank = 0;
    private RealMatrix root;

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
            throws NonPositiveDefiniteMatrixException {

        if (matrix == null) {
            throw new IllegalArgumentException("Matrix must not be null");
        }

        final int order = matrix.getRowDimension();
        final double[][] c = matrix.getData();
        final double[][] b = new double[order][order];

        int[] swap  = new int[order];
        int[] index = new int[order];
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        int r = 0;
        while (r < order) {

            // find maximal diagonal element
            swap[r] = r;
            for (int i = r + 1; i < order; ++i) {
                int ii  = index[i];
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
                        // there is at least one sufficiently negative diagonal element,
                        // the symmetric positive semidefinite matrix is wrong
                        throw new NonPositiveDefiniteMatrixException(c[index[i]][index[i]], i, small);
                    }
                }

                // all remaining diagonal elements are close to zero, we consider we have
                // found the rank of the symmetric positive semidefinite matrix
                ++r;

            } else {

                // transform the matrix
                final double sqrt = Math.sqrt(c[ir][ir]);
                b[r][r] = sqrt;
                final double inverse  = 1 / sqrt;
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
                r++;
            }
        }

        // build the root matrix
        rank = r;
        this.root = new Array2DRowRealMatrix(order, r);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < r; ++j) {
                this.root.setEntry(index[i], j, b[i][j]);
            }
        }
    }

    public RealMatrix getRootMatrix() {
        return root;
    }
}