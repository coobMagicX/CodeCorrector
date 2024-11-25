protected void incrementIterationsCounter() {
    iterationsCounter++;
}

protected RealPointValuePair evaluateNewSimplex(final RealPointValuePair[] original, final double factor, final Comparator<RealPointValuePair> comparator) throws FunctionEvaluationException {
    // implementation to evaluate a new simplex based on the given parameters
}

protected boolean isConverged(RealPointValuePair[] simplex, int iterationsCounter) {
    // implementation to check if the simplex has converged based on the iteration counter and function values
}