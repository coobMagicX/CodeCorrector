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

            // check convergence and boundary checks
            if (isConverged() && isWithinBoundary(reflected)) {
                return;
            }

        }

    }

}

private boolean isConverged() {
    return iterationsCounter >= evaluationsLimit;
}

private boolean isWithinBoundary(final RealPointValuePair point) {
    final double[] x = point.getPointRef();
    for (double value : x) {
        if (value < Double.MIN_VALUE || value > Double.MAX_VALUE) {
            return false;
        }
    }
    return true;
}