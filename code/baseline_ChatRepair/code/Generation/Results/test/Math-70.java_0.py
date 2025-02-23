public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    if (f == null) {
        throw new FunctionEvaluationException("Function must not be null");
    }
    setup(f); // Assuming there is a setup method that