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
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = new RealPointValuePair[] {expanded};
            } else {
                // accept the reflected simplex
                simplex = new RealPointValuePair[] {reflected};
            }
            // check convergence
            if (checkConvergence()) return;
            continue;
        }

        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = new RealPointValuePair[] {contracted};
        } else {
            // no improvement, shrink the simplex around the best point
            simplex = shrinkSimplex(original);
        }

        // check convergence
        if (checkConvergence()) return;
    }
}

private RealPointValuePair[] shrinkSimplex(final RealPointValuePair[] original) {
    final int n = original.length;
    final RealPointValuePair best = original[0];
    RealPointValuePair[] newSimplex = new RealPointValuePair[n];
    newSimplex[0] = best;

    for (int i = 1; i < n; ++i) {
        final double[] xOriginal = original[i].getPointRef();
        final double[] xBest = best.getPointRef();
        double[] xNew = new double[xOriginal.length];
        for (int j = 0; j < xOriginal.length; ++j) {
            xNew[j] = xBest[j] + 0.5 * (xOriginal[j] - xBest[j]);
        }
        newSimplex[i] = new RealPointValuePair(xNew, Double.NaN, false);
    }
    evaluateSimplex(new Comparator<RealPointValuePair>());
    return newSimplex;
}

private boolean checkConvergence() {
    // Implement convergence checking logic
    // This is a placeholder for actual implementation
    return getEvaluations() < 150;
}