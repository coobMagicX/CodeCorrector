public double solve(final UnivariateRealFunction f, double min, double max, double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {
    if (f == null) {
        throw new NullPointerException("The function provided is null.");
    }
    
    // Assuming you have a method to set the initial estimate