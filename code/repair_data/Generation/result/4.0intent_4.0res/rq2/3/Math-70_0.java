public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    // Assuming the solve method should use the initial value correctly,
    // and considering this might be related to an iterative method such as bisection.
    // We adjust the previous fix to ensure that the initial value is considered as a possible midpoint.
    if (f.value(initial) == 0) {
        return initial;
    } else if (f.value(min) * f.value(initial) < 0) {
        return solve(f, min, initial);
    } else {
        return solve(f, initial, max);
    }
}