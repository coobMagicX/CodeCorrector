public class BisectionSolver {
    private static final int MAX_ITERATIONS = 1000;
    private static final double ACCURACY = 1e-7;

    public double solve(final UnivariateRealFunction f, double min, double max, double initial)
            throws MaxIterationsExceededException, FunctionEvaluationException {

        if (initial < min || initial > max) {
            throw new IllegalArgumentException("Initial guess must be within the interval [min, max]");
        }

        int iterations = 0;
        while (Math.abs(f.evaluate(initial)) > ACCURACY && iterations < MAX_ITERATIONS) {
            double mid = (min + max) / 2.0;
            if (f.evaluate(mid) * f.evaluate(initial) < 0) {
                max = initial;
            } else {
                min = initial;
            }
            initial = mid;
            iterations++;
        }

        if (iterations >= MAX_ITERATIONS) {
            throw new MaxIterationsExceededException("Maximum number of iterations reached.");
        }

        return initial;
    }
}