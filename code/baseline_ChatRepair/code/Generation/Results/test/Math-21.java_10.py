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
    while (r < order) {
        // find the largest diagonal element in the remaining part of the matrix
        int maxIndex = r;
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[maxIndex]][index[maxIndex]]) {
                maxIndex = i;
            }
        }

        // swap both the indices and the rows in b
        int temp = index[r];
        index[r] = index[maxIndex];
        index[maxIndex] = temp;

        // Check the new pivot element
        int ir = index[r];
        if (c[ir][ir] < small) {
            // Check if it is sufficiently small to consider the matrix rank deficient
            boolean continueDecomposition = true;
            for (int i = r; i < order; ++i) {
                if (c[index[i]][index[i]] < -small) {
                    throw new NonPositiveDefiniteMatrixException(c[index[i]][index[i]], index[i], small);
                }
                if (Math.abs(c[index[i]][index[i]]) > small) {
                    continueDecomposition = false;
                }
            }
            if (continueDecomposition) {
                break; // exit the loop as the remaining matrix is negligible
            }
        }

        double sqrt = Math.sqrt(c[ir][ir]);
        b[r][r] = sqrt;
        double inverse = 1 / sqrt;

        for (int i = r + 1; i < order; ++i) {
            int ii = index[i];
            double e = c[ii][ir] / sqrt;
            b[i][r] = e;
            c[ii][ii] -= e * e;
            for (int j = r + 1; j < i; ++j) {
                int ij = index[j];
                c[ii][ij] -= e * b[j][r];
                c[ij][ii] = c[ii][ij];  // Maintain symmetry
            }
        }
        r++;
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, rank);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < rank; ++j) {
            root.setEntry(index[i], j, b[index[i]][j]);
        }
    }
}
