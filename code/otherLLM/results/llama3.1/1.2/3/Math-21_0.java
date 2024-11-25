public class RectangularCholeskyDecomposition {

    private int rank;
    private double[][] root;

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small) throws NonPositiveDefiniteMatrixException {
        final int order = matrix.getRowDimension();
        final double[][] c = matrix.getData();

        int[] swap = new int[order];
        int[] index = new int[order];

        for (int i = 0; i < order; i++) {
            index[i] = i;
        }

        int r = 0;

        while (true) {
            // Find maximal diagonal element
            swap[r] = r;
            for (int i = r + 1; i < order; i++) {
                int ii = index[i];
                int isi = index[swap[i]];
                if (c[ii][ii] > c[isi][isi]) {
                    swap[r] = i;
                }
            }

            // Swap elements
            if (swap[r] != r) {
                int tmp = index[r];
                index[r] = index[swap[r]];
                index[swap[r]] = tmp;
            }

            // Check diagonal element
            int ir = index[r];
            if (c[ir][ir] < small) {
                if (r == 0) {
                    throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
                }

                // Check remaining diagonal elements
                for (int i = r; i < order; i++) {
                    if (c[index[i]][index[i]] < -small) {
                        throw new NonPositiveDefiniteMatrixException(c[index[i]][index[i]], i, small);
                    }
                }

                // Consider rank of symmetric positive semidefinite matrix found
                ++r;
                break;
            } else {
                // Transform matrix
                double sqrt = Math.sqrt(c[ir][ir]);
                root[r][r] = sqrt;
                double inverse = 1 / sqrt;

                for (int i = r + 1; i < order; i++) {
                    int ii = index[i];
                    double e = inverse * c[ii][ir];
                    root[i][r] = e;
                    c[ii][ii] -= e * e;

                    for (int j = r + 1; j < i; j++) {
                        int ij = index[j];
                        double f = c[ii][ij] - e * root[j][r];
                        c[ii][ij] = f;
                        c[ij][ii] = f;
                    }
                }

                // Prepare next iteration
                r++;
            }
        }

        rank = r;
        this.root = MatrixUtils.createRealMatrix(order, r);
        for (int i = 0; i < order; i++) {
            for (int j = 0; j < r; j++) {
                root.setEntry(index[i], j, root[i][j]);
            }
        }
    }

    public int getRank() {
        return rank;
    }

    public double[][] getRootMatrix() {
        return root;
    }
}