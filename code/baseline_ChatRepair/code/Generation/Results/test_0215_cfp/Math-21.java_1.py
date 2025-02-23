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
    boolean reduceRank = false;
    for (int count = order; (count > 0) && !reduceRank; count--) {
        // find pivot (largest diagonal element)
        int pivot = r;
        for (int i = r + 1; i < order; i++) {
            if (c[index[i]][index[i]] > c[index[pivot]][index[pivot]]) {
                pivot = i;
            }
        }

        // swap index[r] with index[pivot] if necessary
        if (pivot != r) {
            int tmp = index[r];
            index[r] = index[pivot];
            index[pivot] = tmp;
        }

        // Check for small diagonal element
        if (c[index[r]][index[r]] < small) {
            if (r == 0) {
                throw new NonPositiveDefiniteMatrixException(c[index[r]][index[r]], r, small);
            }
            // Discard trailing elements that are too small
            reduceRank = true;
            continue;
        }

        // Perform Cholesky factor extraction
        double sqrt = Math.sqrt(c[index[r]][index[r]]);
        b[r][r] = sqrt;
        double inverse = 1.0 / sqrt;
        for (int i = r + 1; i < order; i++) {
            b[i][r] = c[index[i]][index[r]] * inverse;
            for (int j = r + 1; j <= i; j++) {
                c[index[i]][index[j]] -= b[i][r] * b[j][r];
                c[index[j]][index[i]] = c[index[i]][index[j]]; // maintain symmetry
            }
        }
        r++;
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, rank);
    for (int i = 0; i < order; i++) {
        for (int j = 0; j < rank; j++) {
            root.setEntry(i, j, b[index[i]][j]);
        }
    }
}
