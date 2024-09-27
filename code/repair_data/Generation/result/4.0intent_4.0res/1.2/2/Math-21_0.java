public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
    throws NonPositiveDefiniteMatrixException {

    final int order = matrix.getRowDimension();
    final double[][] c = matrix.getData();
    final double[][] b = new double[order][order];

    int[] swap  = new int[order];
    int[] index = new int[order];
    for (int i = 0; i < order; ++i) {
        index[i] = i;
    }

    int r = 0;
    for (boolean loop = true; loop;) {

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
        if (c[ir][ir] <= small) { // Adjusted condition to handle zero and near-zero diagonal elements

            if (c[ir][ir] < -small) {
                // there is a significantly negative diagonal element
                throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
            }

            // If diagonal element is zero or very small, skip the row
            if (Math.abs(c[ir][ir]) <= small) {
                index[r] = index[order - 1]; // Move the index of the zero row to the end
                order--; // Reduce the order to effectively ignore this row/column
                continue; // Skip processing this row/column
            }
        }

        // transform the matrix
        final double sqrt = Math.sqrt(Math.max(c[ir][ir], 0)); // Ensure non-negative under the square root
        b[r][r] = sqrt;
        final double inverse = sqrt == 0 ? 0 : 1 / sqrt; // Prevent division by zero
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

    // build the root matrix
    rank = r;
    root = MatrixUtils.createRealMatrix(order, r);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < r; ++j) {
            root.setEntry(index[i], j, b[i][j]);
        }
    }
}