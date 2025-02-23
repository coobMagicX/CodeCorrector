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
    boolean loop = true;
    while (loop) {

        // find maximal diagonal element
        int maxIndex = r;
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[maxIndex]][index[maxIndex]]) {
                maxIndex = i;
            }
        }

        // swap elements
        if (maxIndex != r) {
            int temp = index[r];
            index[r] = index[maxIndex];
            index[maxIndex] = temp;
        }

        // check diagonal element
        int ir = index[r];
        if (c[ir][ir] < small) {
            if (r == 0) {
                throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
            }
            loop = false; // Break out if matrix is rank deficient
        } else {
            // transform the matrix
            double sqrt = Math.sqrt(c[ir][ir]);
            b[r][r] = sqrt;
            double inverse = 1.0 / sqrt;
            for (int i = r + 1; i < order; ++i) {
                int ii = index[i];
                double e = inverse * c[ii][ir];
                b[i][r] = e;
                c[ii][ii] -= e * e;
                for (int j = r + 1; j < i; ++j) {
                    int ij = index[j];
                    double f = c[ii][ij] - e * b[j][r];
                    c[ii][ij] = f;
                    c[ij][ii] = f;
                }
            }
            r++;
            if (r == order) loop = false;
        }
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, r);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < r; ++j) {
            root.setEntry(index[i], j, b[i][j]);
        }
    }
}
