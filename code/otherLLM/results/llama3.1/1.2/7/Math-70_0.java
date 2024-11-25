public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    verifyInterval(min,max); // Add validation of input range
    return solve(f, Math.max(min,initial), Math.min(max,initial)); // Clarify argument order in `solve` method and correct solver usage
}