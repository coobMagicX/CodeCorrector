public double solve(final UnivariateRealFunction f, double min, double max, double initial) 
throws MaxIterationsExceededException, FunctionEvaluationException {
    final int maxIterations = 1000; // Maximum number of iterations to prevent infinite loops
    double tolerance = 1e-7; // Tolerance level for the accuracy of the root

    if (f.value(initial) == 0.0) {
        return initial;
    }

    double a = min, b = max;
    int iter = 0;

    while ((b - a) / 2.0 > tolerance && iter < maxIterations) {
        double mid = (a + b) / 2.0;

        if (f.value(mid) == 0.0) {
            return mid; // The midpoint is the root
        } else if (f.value(a) * f.value(mid) > 0) {
            a = mid;
        } else {
            b = mid;
        }
        iter++;
    }

    if (iter >= maxIterations) {
        throw new MaxIterationsExceededException("Maximum number of iterations reached");
    }

    return (a + b) / 2.0; // Return the midpoint as an approximate root
}