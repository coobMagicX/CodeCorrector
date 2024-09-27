public double chiSquare(double[] expected, long[] observed)
    throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be postive");
    }
    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
            dev = ((double) observed[i] - expected[i]);
            sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

public double chiSquare(long[][] counts) throws IllegalArgumentException {
    checkArray(counts);
    int nRows = counts.length;
    int nCols = counts[0].length;
    
    // compute row, column and total sums
    double[] rowSum = new double[nRows];
    double[] colSum = new double[nCols];
    double total = 0.0d;
    for (int row = 0; row < nRows; row++) {
        for (int col = 0; col < nCols; col++) {
            rowSum[row] += (double) counts[row][col];
            colSum[col] += (double) counts[row][col];
            total += (double) counts[row][col];
        }
    }
    
    // compute expected counts and chi-square
    double sumSq = 0.0d;
    double expected = 0.0d;
    for (int row = 0; row < nRows; row++) {
        for (int col = 0; col < nCols; col++) {
            expected = (rowSum[row] * colSum[col]) / total;
            sumSq += (((double) counts[row][col] - expected) * 
                    ((double) counts[row][col] - expected)) / expected; 
        }
    } 
    return sumSq;
}