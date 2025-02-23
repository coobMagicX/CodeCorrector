import java.math.BigDecimal;

public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    BigDecimal sumSq = BigDecimal.ZERO;
    BigDecimal dev;
    BigDecimal e;
    for (int i = 0; i < observed.length; i++) {
        e = BigDecimal.valueOf(expected[i]);
        dev = BigDecimal.valueOf(observed[i]).subtract(e);
        // Calculate (dev * dev) / expected[i]
        BigDecimal term = dev.multiply(dev).divide(e, MathContext.DECIMAL128);
        sumSq = sumSq.add(term);
    }
    return sumSq.doubleValue();
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
