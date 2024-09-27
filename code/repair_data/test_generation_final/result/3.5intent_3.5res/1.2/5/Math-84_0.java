protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int maxIterations = getMaxIterations();
    int maxEvaluations = getMaxEvaluations();

    double epsilon = getEpsilon();

    int iteration = 0;
    int evaluations = 0;

    while (iteration < maxIterations && evaluations < maxEvaluations) {

        incrementIterationsCounter();
        iteration++;

        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        evaluations++;
        if (comparator.compare(reflected, best) < 0) {

            // compute the expanded simplex
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            evaluations++;
            if (comparator.compare(reflected, expanded) <= 0) {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            }

            return;

        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        evaluations++;
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex

            // check convergence
            double diff = Math.abs(best.getValue() - contracted.getValue());
            if (diff < epsilon) {
                return;
            }
        }

    }

}