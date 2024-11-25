protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

while (true) {

    incrementIterationsCounter();

    // save the original vertex
    final RealPointValuePair[] original = simplex;
    final RealPointValuePair best = original[0];

    // perform a reflection step
    final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
    if (comparator.compare(reflected, best) < 0) {

        // compute the expanded simplex
        final RealPointValuePair[] reflectedSimplex = simplex;
        final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
        if (comparator.compare(reflected, expanded) <= 0) {
            // accept the reflected simplex
            simplex = reflectedSimplex;
        }

        return;

    }

    // compute the contracted simplex
    final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
    if (comparator.compare(contracted, best) < 0) {
        // accept the contracted simplex

        // check convergence
        if (isConverged(comparator)) {
            return;
        }
    }

}

}

private boolean isConverged(final Comparator<RealPointValuePair> comparator) {
    final RealPointValuePair[] original = simplex;
    final RealPointValuePair best = original[0];
    final RealPointValuePair worst = original[simplex.length - 1];

    // check if the worst vertex is within a certain tolerance of the best
    return Math.abs(comparator.compare(best, worst)) < 1e-6;
}