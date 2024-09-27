public class LinearCombinationCalculator {

    // Define the split factor for numerical stability
    private static final double SPLIT_FACTOR = 134217729; // 2^27 + 1

    // Custom exception for dimension mismatch
    public static class DimensionMismatchException extends RuntimeException {
        public DimensionMismatchException(int len1, int len2) {
            super("Dimensions mismatch: " + len1 + " != " + len2);
        }
    }

    public static double linearCombination(final double[] a, final double[] b) throws DimensionMismatchException {
        final int len = a.length;
        if (len != b.length) {
            throw new DimensionMismatchException(len, b.length);
        }

        final double[] prodHigh = new double[len];
        double prodLowSum = 0;

        for (int i = 0; i < len; i++) {
            final double ai = a[i];
            final double ca = SPLIT_FACTOR * ai;
            final double aHigh = ca - (ca - ai);
            final double aLow = ai - aHigh;

            final double bi = b[i];
            final double cb = SPLIT_FACTOR * bi;
            final double bHigh = cb - (cb - bi);
            final double bLow = bi - bHigh;

            prodHigh[i] = aHigh * bHigh;
            final double prodLow = aLow * bLow - (((prodHigh[i] - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow);
            prodLowSum += prodLow;
        }

        double result = 0;
        for (double high : prodHigh) {
            result += high;
        }
        result += prodLowSum;

        if (Double.isNaN(result)) {
            result = 0;
            for (int i = 0; i < len; ++i) {
                result += a[i] * b[i];
            }
        }

        return result;
    }

    public static void main(String[] args) {
        double[] a = {1.0, 2.0, 3.0};
        double[] b = {4.0, 5.0, 6.0};
        try {
            double result = linearCombination(a, b);
            System.out.println("Result: " + result);
        } catch (DimensionMismatchException e) {
            System.err.println(e.getMessage());
        }
    }
}