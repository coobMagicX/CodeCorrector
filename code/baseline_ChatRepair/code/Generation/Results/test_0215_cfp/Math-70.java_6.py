public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    if (f == null) {
        throw new NullPointerException("Function must not be null.");
    }
    setup(f, min, max, initial);
    return doSolve();
}
