protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int maxIterations = 150; // Maximum number of iterations allowed
    boolean isConverged = false;

    while (!isConverged && incrementIterationsCounter() < maxIterations) {
        
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(reflected, expanded) <= 0) {
                simplex = reflectedSimplex;
            }
            isConverged = true; // Assuming we've found a better solution
        } else {
            final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                simplex = original; // Replace with the contracted simplex
                isConverged = true; // Assuming we've found a better solution
            }
        }

        if (!isConverged && incrementIterationsCounter() >= maxIterations) {
            throw new OptimizationException("Optimizer did not converge within the expected number of evaluations.");
        }
    }
}