public class RootFinder {

    private static final int MAXIMAL_AGING = 10;
    private static final double REDUCTION_FACTOR = 0.5;

    public static double guessX(double targetY, double[] x, double[] y, int start, int end) {
        // Use Lagrange interpolation instead of inverse polynomial interpolation
        for (int i = start; i < end; i++) {
            // ...
        }
    }

    public static double computeObjectiveValue(double nextX) {
        // Cache function evaluations if possible
        if (cache.containsKey(nextX)) {
            return cache.get(nextX);
        } else {
            // Evaluate the function at nextX and store the result in the cache
            return evaluateFunctionAt(nextX);
        }
    }

    public static boolean Precision.equals(double a, double b, double tolerance) {
        // Use Math.abs() for comparing floating-point numbers
        return Math.abs(a - b) < tolerance;
    }

    public static void main(String[] args) {
        // ...
    }
}