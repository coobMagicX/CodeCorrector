import static java.lang.Math.*;

public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Check for extreme values to avoid errors in erf computation
        if (dev == Double.MAX_VALUE || dev == Double.NEGATIVE_INFINITY) {
            return dev > 0 ? 1.0 : 0.0; // For positive infinity, probability should be 1, and for negative infinity, it should be 0.
        }
        return 0.5 * (1.0 + Erf.erf(dev / (standardDeviation * sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (dev < -20 * standardDeviation) { // JDK 1.5 blows at 38
            return 0;
        } else if (dev > 20 * standardDeviation) {
            return 1;
        } else {
            throw ex;
        }
    }
}