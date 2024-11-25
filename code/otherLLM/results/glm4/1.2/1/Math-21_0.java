public RectangularCholeskyDecomposition(RealMatrix matrix, double small)
throws NonPositiveDefiniteMatrixException {

    final int order = matrix.getRowDimension();
    final double[][] c = matrix.getData();
    final double[][] b = new double[order][order];

    // Initialize a matrix to hold the permutation
    int[] permuteOrder = new int[order];
    for (int i = 0; i < order; ++i) {
        permuteOrder[i] = i;
    }

    int r = 0;
    while (r < order) {

        // Find the maximum diagonal element in the current column
        int maxIndex = r;
        for (int i = r + 1; i < order; ++i) {
            if (Math.abs(c[permuteOrder[i]][permuteOrder[i]]) > Math.abs(c[permuteOrder[maxIndex]][permuteOrder[maxIndex]])) {
                maxIndex = i;
            }
        }

        // If the diagonal element is less than small, it's non-positive
        if (Math.abs(c[permuteOrder[maxIndex]][permuteOrder[maxIndex]]) < small) {
            throw new NonPositiveDefiniteMatrixException(
                    c[permuteOrder[maxIndex]][permuteOrder[maxIndex]], maxIndex, small);
        }

        // Swap the found maximum element with the current row
        int tmp = permuteOrder[r];
        permuteOrder[r] = permuteOrder[maxIndex];
        permuteOrder[maxIndex] = tmp;

        // Perform Cholesky decomposition
        double sqrtDiag = Math.sqrt(c[permuteOrder[r]][permuteOrder[r]]);
        b[r][r] = sqrtDiag;
        for (int i = r + 1; i < order; ++i) {
            b[i][r] = c[permuteOrder[i]][permuteOrder[r]] / sqrtDiag;
        }
        for (int i = r + 1; i < order; ++i) {
            double sum = 0.0;
            for (int k = 0; k <= r - 1; ++k) {
                sum += b[k][r] * b[i][k];
            }
            b[i][r] -= sum;
        }

        // Construct the next diagonal block
        for (int i = r + 1; i < order; ++i) {
            double inverseDiag = Math.abs(c[permuteOrder[i]][permuteOrder[i]]) > small ? 1 / Math.sqrt(c[permuteOrder[i]][permuteOrder[i]]) : 0;
            for (int j = r + 1; j < order; ++j) {
                c[permuteOrder[i]][permuteOrder[j]] -= b[i][r] * b[j][r];
                if (i != j) {
                    c[permuteOrder[j]][permuteOrder[i]] -= b[j][r] * b[i][r];
                }
            }
        }

        // Move to the next column
        r++;
    }

    // Build the root matrix from the upper triangular part of B
    RealMatrix root = MatrixUtils.createRealMatrix(order, order);
    for (int i = 0; i < order; ++i) {
        for (int j = 0; j <= i; ++j) {
            root.setEntry(i, j, b[i][j]);
        }
    }

    this.root = root;
}

// The 'root' variable should be declared and initialized outside of the constructor
private RealMatrix root;