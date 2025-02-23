public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    // Assuming there exists a method that uses the initial value for better guess placement
    return solve(f, min, max, initial);
}
