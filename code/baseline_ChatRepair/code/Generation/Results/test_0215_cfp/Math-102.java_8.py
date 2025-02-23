import java.math.BigDecimal;
import java.math.MathContext;

public double chiSquare(double[] expected, long[] observed)
    throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "Observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0;
    for (int i = 0; i < observed.length; i++) {
        double dev = observed[i] - expected[i];
        sumSq += (dev * dev) / expected[i];
    }
    return sumSq;
}

private boolean isPositive(double[] array) {
    for (double v : array) {
        if (v <= 0) {
            return false;
        }
    }
    return true;
}

private boolean isNonNegative(long[] array) {
    for (long v : array) {
        if (v < 0) {
            return false;
        }
    }
    return true;
}
