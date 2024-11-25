public double solve(final UnivariateRealFunction f, double min, double max, double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {
    verifyInterval(min,max); // Validate input parameters
    return solve(f, min, max); // Correctly implement numerical solver logic using existing method
}