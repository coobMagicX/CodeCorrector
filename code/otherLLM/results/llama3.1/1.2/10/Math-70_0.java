public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    return solve(f, Math.min(initial, max), Math.max(initial, min));
}