protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    int maxIterations = getMaxIterations();
    while (getIterationCounter() < maxIterations) {
        
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
            boolean isConverged = checkConvergence(simplex, comparator);
            if (isConverged) {
                return;
            }
        }

    }

    throw new OptimizationException("Maximum number of iterations reached without convergence.");
}

private boolean checkConvergence(final RealPointValuePair[] simplex, final Comparator<RealPointValuePair> comparator) {
    // Implementation for checking convergence
    // This method should determine if the points in the simplex are sufficiently close to each other or within an acceptable range.
    // For example:
    double minDistance = Double.MAX_VALUE;
    for (int i = 1; i < simplex.length - 1; ++i) {
        double distance = getDistance(simplex[i].getPointRef(), simplex[i + 1].getPointRef());
        if (distance < minDistance) {
            minDistance = distance;
        }
    }
    return minDistance <= convergenceThreshold;
}

private double getDistance(final double[] point1, final double[] point2) {
    // Implementation for calculating the Euclidean distance between two points
    double sumOfSquares = 0.0;
    for (int i = 0; i < point1.length; ++i) {
        sumOfSquares += Math.pow(point1[i] - point2[i], 2);
    }
    return Math.sqrt(sumOfSquares);
}