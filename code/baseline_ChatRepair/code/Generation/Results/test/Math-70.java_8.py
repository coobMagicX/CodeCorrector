public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    if (solver instanceof BaseAbstractUnivariateSolver) {
        ((BaseAbstractUnivariateSolver)solver).setInitialValue(initial);
    }
    return solve(min