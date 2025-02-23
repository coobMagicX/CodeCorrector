import org.apache.commons.math3.stat.inference.ChiSquareTest;

public double chiSquare(double[] expected, long[] observed) {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException(
                "observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException(
            "observed counts must be non-negative and expected counts must be positive");
    }
    ChiSquareTest chiSquareTest = new ChiSquareTest();
    return chiSquareTest.chiSquare(expected, observed);
}
