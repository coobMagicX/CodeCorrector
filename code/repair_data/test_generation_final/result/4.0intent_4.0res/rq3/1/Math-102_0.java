public class ChiSquareCalculator {

    public double chiSquare(double[] expected, long[] observed) {
        if ((expected.length < 2) || (expected.length != observed.length)) {
            throw new IllegalArgumentException(
                    "observed and expected array lengths are incorrect");
        }
        if (!isPositive(expected) || !isNonNegative(observed)) {
            throw new IllegalArgumentException(
                "Expected counts must be positive and observed counts must be non-negative");
        }
        double sumSq = 0.0;
        double dev;
        for (int i = 0; i < observed.length; i++) {
            dev = ((double) observed[i] - expected[i]);
            sumSq += dev * dev / expected[i];
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
}