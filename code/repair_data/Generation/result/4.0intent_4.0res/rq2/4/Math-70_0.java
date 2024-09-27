public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    // Incorporate a check to use 'initial' correctly in the context of bisection method.
    // The bisection method typically does not start from 'initial', but it ensures 'initial' is within the range [min, max].
    // Assuming 'initial' must be between 'min' and 'max', and we use it to possibly refine our approach or as a fallback.

    if (initial < min || initial > max) {
        throw new IllegalArgumentException("Initial value must be between min and max.");
    }

    // Use bisection method logic here, starting at the midpoint of min and max.
    double mid = (min + max) / 2;
    while (Math.abs(max - min) > 1e-10) { // assuming some tolerance
        mid = (min + max) / 2;
        if (Math.signum(f.value(mid)) == Math.signum(f.value(min))) {
            min = mid;
        } else {
            max = mid;
        }
    }
    return mid; // Return the midpoint which is the best guess for root.
}