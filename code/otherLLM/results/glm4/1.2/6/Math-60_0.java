import org.apache.commons.math3.special.Erf;

public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        // Use Erf.erf directly instead of dividing by FastMath.sqrt(2.0) to avoid unnecessary division.
        return 0.5 * (1.0 + Erf.erf(dev / (standardDeviation * 1.4142135623730951)));
    } catch (MaxIterationsExceededException ex) {
        // Check for extreme values as previously done, but make sure to handle infinity explicitly.
        if (Double.isInfinite(x)) {
            return x > mean ? 1 : 0;
        } else if (x < mean - 20 * standardDeviation) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > mean + 20 * standardDeviation) {
            return 1;
        } else {
            throw ex;
        }
    }
}