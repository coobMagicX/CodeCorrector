public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    // Update the initial value passed to the solve(min, max) call
    return solve(f, Math.min(initial, min), Math.max(initial, max));
}