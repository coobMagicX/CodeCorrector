public double getSumSquaredErrors() {
    double regressionSumOfSquares = (sumXY * sumXY) / sumXX;
    if (sumYY >= regressionSumOfSquares) {
        return