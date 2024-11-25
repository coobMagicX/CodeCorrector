import static java.lang.Math.*;

public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;

    // Check for extreme values of 'x' and handle them accordingly to avoid overflow
    if (dev > 20 * standardDeviation || dev < -20 * standardDeviation) {
        return dev > 0 ? 1.0 : 0.0;
    }

    try {
        return 0.5 * (1.0 + erf((dev) / (standardDeviation * sqrt(2.0))));
    } catch (MathException ex) {
        if (x < mean - 20 * standardDeviation) { // JDK 1.5 blows at 38
            return 0;
        } else if (x > mean + 20 * standardDeviation) {
            return 1;
        } else {
            throw new MathException("Error in cumulativeProbability calculation");
        }
    }
}