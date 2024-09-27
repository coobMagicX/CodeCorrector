public double chiSquare(long[][] counts) throws IllegalArgumentException {
    checkArray(counts);
    int nRows = counts.length;
    int nCols = counts[0].length;
    
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
    
    double sumSq = 0.0d;
    double expected = 0.0d;
    for (int row = 0; row < nRows; row++) {
        for (int col = 0; col < nCols; col++) {
            expected = (rowSum[row] * colSum[col]) / total;
            sumSq += Math.pow(((double) counts[row][col] - expected), 2) / expected;
        }
    } 
    return sumSq;
}