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
    while (loop && r < order) {
        // find maximum diagonal element
        int maxIndex = r;
        for (int i = r + 1; i < order; ++i) {
            if (c[index[i]][index[i]] > c[index[maxIndex]][index[maxIndex]]) {
                maxIndex = i;
            }
        }

        // swap indices
        if (maxIndex != r) {
            int temp = index[r];
            index[r] = index[maxIndex];
            index[maxIndex] = temp;
        }

        int ir = index[r];
        if (c[ir][ir] < small) {
            if (r == 0) {
                throw new NonPositiveDefiniteMatrixException(c[ir][ir], ir, small);
            }

            for (int i = r; i < order; ++i) {
                int ii = index[i];
                if (c[ii][ii] < -small) {
                    throw new NonPositiveDefiniteMatrixException(c[ii][ii], i, small);
                }
            }

            r++;
            loop = false;
        } else {
            double sqrt = Math.sqrt(c[ir][ir]);
            b[r][r] = sqrt;
            double inverse = 1 / sqrt;
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
            loop = r < order;
        }
    }

    rank = r;
    root = MatrixUtils.createRealMatrix(order, r);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j < r; ++j) {
            root.setEntry(i, j, b[index[i]][j]);
        }
    }
}
