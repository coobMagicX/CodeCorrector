public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    double a = Math.min(min, initial); // Ensure that a is the smaller value between min and initial
    double b = Math.max(min, initial); // Ensure that b is the larger value between min and initial

    double fa = f.value(a);
    double fb = f.value(b);

    if (fa * fb > 0) {
        throw new FunctionEvaluationException("Function values must have opposite signs.");
    }

    int maxIterations = 100; // Assuming a fixed number of iterations for the example
    double tolerance = 1e-10; // The desired accuracy

    for (int iteration = 0; iteration < maxIterations; ++iteration) {
        if (Math.abs(b - a) <= tolerance) { // If the interval is small enough, stop iterating
            break;
        }

        double mid = (a + b) / 2.0;
        double fm = f.value(mid);

        if (fm == 0) { // The root has been found exactly
            return mid;
        } else if (fa * fm < 0) {
            b = mid; // Root is between a and mid
            fb = fm;
        } else {
            a = mid; // Root is between mid and b
            fa = fm;
        }
    }

    throw new MaxIterationsExceededException("Maximum number of iterations reached.");
}