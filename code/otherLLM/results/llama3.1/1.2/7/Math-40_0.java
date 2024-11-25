public double guessX(double targetY, double[] x, double[] y, int start, int end) {
    if ((end - start) == 1) return (x[end] + x[start]) / 2;

    double m = (y[end] - y[start]) / (x[end] - x[start]);
    double b = y[start] - m * x[start];
    return -(targetY - b) / m;
}