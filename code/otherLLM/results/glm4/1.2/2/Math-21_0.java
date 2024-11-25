public class RectangularCholeskyDecomposition {

    private double[][] data;
    private int rank = 0;

    // Existing constructor not shown here for brevity

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small) throws NonPositiveDefiniteMatrixException {
        final int order = matrix.getRowDimension();
        this.data = matrix.getData();
        int[] swap = new int[order];
        int[] index = new int[order];
        
        for (int i = 0; i < order; ++i) {
            index[i] = i;
        }

        boolean loop = true;
        while (loop) {
            // find maximal diagonal element
            int r = 0;
            double maxDiagonal = Double.MIN_VALUE;
            for (int i = 0; i < order; ++i) {
                int ii = index[i];
                if (data[ii][ii] > maxDiagonal) {
                    maxDiagonal = data[ii][ii];
                    r = i;
                }
            }

            // swap elements
            swap[r] = r;
            for (int i = 0; i < order; ++i) {
                int ir = index[swap[i]];
                if (data[ir][ir] > maxDiagonal) {
                    swap[i] = r;
                } else {
                    swap[i] = ir;
                }
            }

            // check diagonal element
            if (data[index[r]][index[r]] < small) {
                throw new NonPositiveDefiniteMatrixException(data[index[r]][index[r]], index[r], small);
            }

            final double sqrt = Math.sqrt(data[index[r]][index[r]]);
            rank++;

            for (int i = 0; i < order; ++i) {
                if (i != r) {
                    double e = -data[index[i]][index[r]] / sqrt;
                    data[index[i]][index[r]] = 0;
                    data[index[r]][index[i]] += e * e;

                    for (int j = 0; j < i; ++j) {
                        int ij = index[j];
                        if (ij != r) {
                            double f = data[index[i]][ij] - e * data[swap[j]][index[r]];
                            data[index[i]][ij] = f;
                            data[swap[j]][ij] = f;
                        }
                    }
                }
            }

            // Prepare for the next iteration
            loop = --r >= 0;

            // Swap back to maintain original indices after processing each row
            if (loop) {
                for (int i = 0; i <= r; ++i) {
                    int tmp = index[i];
                    index[i] = swap[i];
                    swap[i] = tmp;
                }
            }
        }

        // build the root matrix
        RealMatrix rootMatrix = MatrixUtils.createRealMatrix(order, rank);
        for (int i = 0; i < order; ++i) {
            for (int j = 0; j < rank; ++j) {
                rootMatrix.setEntry(i, j, data[index[i]][index[j]]);
            }
        }
    }

    // Other methods and class members not shown
}