public double solve(final UnivariateRealFunction f, double min, double max, double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {
    BisectionSolver solver = new BisectionSolver(f, initial);
    solver.configure(min, max);
    return Math.max(solver.solve(), -solver.getAbsoluteAccuracy() + initial);
}