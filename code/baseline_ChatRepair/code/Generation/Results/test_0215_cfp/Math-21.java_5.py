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
    for (boolean loop = true; loop && r < order;) {

        // find maximal diagonal element
        int max = r;
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[max]][index[max]]) {
                max = i;
            }
        }

        // swap elements
        if (max != r) {
            int tmp = index[r];
            index[r] = index[max];
            index[max] = tmp;
        }

        // check diagonal element
        if (c[index[r]][index[r]] < small) {
            if (r == 0) {
                throw new NonPositiveDefiniteMatrixException(c[index[r]][index[r]], r, small);
            }

            // all diagonal elements index[r] to index[order-1] should be close to zero
            for (int i = r; i < order; ++i) {
                int idx = index[i];
                if (c[idx][idx] < -small) {
                    throw new NonPositiveDefiniteMatrixException(c[idx][idx], idx, small);
                }
            }

            loop = false; // found the rank, remaining diagonals are too small
        } else {
            // valid diagonal element, proceed with decomposition
            final double sqrt = Math.sqrt(c[index[r]][index[r]]);
            b[r][r] = sqrt;
            for (int i = r + 1; i < order; ++i) {
                int idx = index[i];
                double factor = c[idx][r] / sqrt;
                b[idx][r] = factor;
                for (int j = r + 1; j <= i; ++j) {
                    c[idx][index[j]] -= factor * b[index[j]][r];
                }
            }
            r++;
        }
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, r);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < r; ++j) {
            root.setEntry(i, j, b[i][j]);
        }
    }
}
