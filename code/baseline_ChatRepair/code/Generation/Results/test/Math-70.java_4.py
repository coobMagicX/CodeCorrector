public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    if (f == null) {
        throw new NullPointerException("Function must not be null.");
    }
    double result = initial; // Start from the initial guess.
    double lastResult;
    double accuracy = get