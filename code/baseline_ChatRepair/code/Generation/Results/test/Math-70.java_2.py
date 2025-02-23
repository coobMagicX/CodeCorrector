public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    if (f == null) {
        throw new IllegalArgumentException("The function must not be null");
    }
    
    // Assuming there's a solver method that handles function 'f', range [min, max], and an initial guess.
    // Make sure