public double guessX(double targetY, double[] x, double[] y) {
    int n = x.length;
    
    // Initialize the result array
    double[][] p = new double[n][n];
    
    // Compute the coefficients for each point
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j <= i; ++j) {
            if (j == 0) {
                p[i][j] = 1;
            } else {
                p[i][j] = (x[i] - x[j - 1]) * p[i][j - 1];
            }
        }
    }
    
    // Compute the polynomial coefficients
    double[] c = new double[n];
    for (int i = 0; i < n; ++i) {
        c[i] = y[i] / p[i][i];
    }
    
    // Find the root using Lagrange interpolation
    double result = targetY;
    for (int i = 0; i < n; ++i) {
        double prod = 1;
        for (int j = 0; j < n; ++j) {
            if (j != i) {
                prod *= (targetY - x[j]) / (x[i] - x[j]);
            }
        }
        result += c[i] * prod;
    }
    
    return result;
}