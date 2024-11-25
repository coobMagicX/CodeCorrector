import static java.lang.Math.*;
public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;

    // Handle extreme values for the erf function
    if (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY) {
        return 1; // For positive infinity, erf returns 1
    } else if (dev > 20 * standardDeviation) { // Assuming this is intended to be the threshold for the upper limit
        return 1;
    } else if (dev < -20 * standardDeviation) { // Assuming this is intended to be the threshold for the lower limit
        return 0;
    }

    try {
        return 0.5 * (1.0 + erf((dev) / (standardDeviation * sqrt(2.0))));
    } catch (ArithmeticException ex) {
        throw new MathException("Math error occurred", ex);
    }
}