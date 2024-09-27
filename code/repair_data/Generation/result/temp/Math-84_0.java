protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int iterationCount = 0;
    while (iterationCount < getMaxIterations()) {

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
            simplex = new RealPointValuePair[] {best, contracted};
        } else {
            // shrink the simplex around the best point
            for (int i = 1; i < simplex.length; i++) {
                final double[] xSmallest = best.getPointRef();
                final double[] xCurrent = simplex[i].getPointRef();
                final double[] xNew = new double[xSmallest.length];
                for (int j = 0; j < xSmallest.length; j++) {
                    xNew[j] = xSmallest[j] + sigma * (xCurrent[j] - xSmallest[j]);
                }
                simplex[i] = new RealPointValuePair(xNew, Double.NaN, false);
            }
        }

        iterationCount++;
    }

}