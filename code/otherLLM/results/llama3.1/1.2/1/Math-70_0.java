public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    return solve(f, min, max);
}

/** {@inheritDoc} */
public double solve(double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
        @Override
        public double value(final double x) {
            // implementation of function here
            return 0;
        }
    };
    return solve(f, min, max);
}

/** {@inheritDoc} */
public double solve(double min, double max)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
        @Override
        public double value(final double x) {
            // implementation of function here
            return 0;
        }
    };
    return solve(f, min, max);
}