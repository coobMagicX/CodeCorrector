public double guessX(double targetY, double[] xValues, double[] yValues, int start, int end) {
    // Lagrange interpolation
    double sum = 0;
    for (int i = start; i <= end; i++) {
        double prod = 1.0;
        for (int j = start; j <= end; j++) {
            if (i != j) {
                prod *= targetY - xValues[j];
            }
        }
        sum += yValues[i] * prod;
    }
    return sum / prod;
}

public void updateBracketingInterval(double nextX, double nextY, double[] xValues, double[] yValues) {
    // Binary search for the tightest bracketing interval
    int start = 0;
    int end = xValues.length - 1;
    while (start < end) {
        int mid = (start + end) / 2;
        if ((nextY * yValues[mid] <= 0)) {
            end = mid;
        } else {
            start = mid + 1;
        }
    }
    // Update the bracketing interval
    xA = nextX;
    yA = nextY;
    absYA = Math.abs(yA);
    agingA = 0;
}