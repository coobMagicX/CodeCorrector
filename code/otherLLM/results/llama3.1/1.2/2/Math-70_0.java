public double solve(final UnivariateRealFunction f, double min, double max, double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min,max);
    return solve(f, Math.max(min, initial), Math.min(max, initial));
}