public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    // Check if the function is indeed provided
    if (f == null) {
        throw new IllegalArgumentException("Function must not be null");
    }
    
    // Now use the solver that takes into account the function 'f' and the initial estimate