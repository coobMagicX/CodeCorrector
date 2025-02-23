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
        // Swap logic refinement
        // find maximal diagonal element
        int swap = r; // set initial swap to diagonal element at r
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[swap]][index[swap]]) {
                swap = i;
            }
        }

        // execute swap if needed
        if (swap != r) {
            int temp = index[r];
            index[r] = index[swap];
            index[swap] = temp;
        }

        // check diagonal element
        if (c[index[r]][index[r]] < small) {
            // handle excessively small or negative diagonal entries
            throw new NonPositiveDefiniteMatrixException(c[index[r]][index[r]], r, small);
        } else {
            // transform the matrix
            final double sqrt = Math.sqrt(c[index[r]][index[r]]);
            b[r][r] = sqrt;
            final double inverse = 1 / sqrt;
            for (int i = r + 1; i < order; ++i) {
                final double e = (c[index[i]][index[r]] /= sqrt);
                b[i][r] = e;
                c[index[i]][index[i]] -= e * e;
                for (int j = r + 1; j < i; ++j) {
                    c[index[i]][index[j]] -= e * b[j][r];
                    c[index[j]][index[i]] = c[index[i]][index[j]]; // enforce symmetry
                }
            }
            r++;
        }
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, rank);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < rank; ++j) {
            root.setEntry(i, j, b[index[i]][j]);
        }
    }
}
