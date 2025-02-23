public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
        throws NonPositiveDefiniteMatrixException {
    
    final int order = matrix.getRowDimension();
    double[][] c = matrix.getData();
    double[][] b = new double[order][order];

    int[] index = new int[order];
    for (int i = 0; i < order; ++i) {
        index[i] = i;
    }

    int r = 0;
    while (r < order) {
        // Find maximal diagonal element
        int maxIndex = r;
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[maxIndex]][index[maxIndex]]) {
                maxIndex = i;
            }
        }

        // Swap indices
        int temp = index[r];
        index[r] = index[maxIndex];
        index[maxIndex] = temp;

        // Check diagonal element
        int ir = index[r];
        if (c[ir][ir] < small) {
            throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
        }

        // Compute sqrt and update matrix
        double sqrt = Math.sqrt(c[ir][ir]);
        b[r][r] = sqrt;
        double inv = 1.0 / sqrt;
        for (int i = r + 1; i < order; ++i) {
            int ii = index[i];
            double e = inv * c[ii][ir];
            b[i][r] = e;
            c[ii][ii] -= e * e;
            for (int j = r + 1; j < i; ++j) {
                int ij = index[j];
                c[ii][ij] -= e * b[j][r];
                c[ij][ii] = c[ii][ij]; // Ensure symmetry is maintained
            }
        }

        r++;
    }

    // Build the root matrix
    rank = r;
    root = MatrixUtils.createRealMatrix(order, r);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < r; ++j) {
            root.setEntry(i, j, b[i][index[j]]);
        }
    }
}
