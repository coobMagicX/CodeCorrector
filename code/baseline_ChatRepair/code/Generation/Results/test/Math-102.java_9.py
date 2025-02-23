import java.math.BigDecimal;

public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    BigDecimal sumSq = BigDecimal.ZERO;
    for (int i = 0; i < observed.length; i++) {
        BigDecimal obs = BigDecimal.valueOf(observed[i]);
        BigDecimal exp = BigDecimal.valueOf(expected[i]);
        BigDecimal dev = obs.subtract(exp);
        BigDecimal sq = dev.multiply(dev).divide(exp, BigDecimal.ROUND_HALF_UP);
        sumSq = sumSq.add(sq);
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
