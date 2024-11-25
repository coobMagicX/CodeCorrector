public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    return solve(f, initial, min, max); // changed to pass 'initial' as the midpoint of min and max in the first iteration
}