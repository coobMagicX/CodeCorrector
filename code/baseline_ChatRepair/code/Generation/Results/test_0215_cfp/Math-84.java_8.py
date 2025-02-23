protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
        throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {
        checkIterationsLimit();  // Check max iterations to prevent infinite loops

        incrementIterationsCounter();
        
        final RealPointValuePair[] original = simplex.clone();
        final RealPointValuePair best = original[0];
        
        // Reflection
        RealPointValuePair[] reflectionSimplex = evaluateNewSimplex(original, 1.0);
        final RealPointValuePair reflected = reflectionSimplex[0];
        
        if (comparator.compare(reflected, best) < 0) {
            // Expansion
            RealPointValuePair[] expandedSimplex = evaluateNewSimplex(original, 2.0);
            final RealPointValuePair expanded = expandedSimplex[0];
            
            if (comparator.compare(expanded, reflected) < 0) {
                simplex = expandedSimplex;  // Accepting expanded simplex
            } else {
                simplex = reflectionSimplex;  // Accepting reflected simplex
            }
            continue;  // Continue to next iteration
        }
        
        // Contraction
        RealPointValuePair[] contractedSimplex = evaluateNewSimplex(original, 0.5);
        final RealPointValuePair contracted = contractedSimplex[0];
        
        if (comparator.compare(contracted, best) < 0) {
            simplex = contractedSimplex;  // Accepting contracted simplex
            continue;  // Continue to next iteration
        }
        
        // Shrink
        shrinkSimplex(best);
        
        // If no step is accepted, check for termination criteria or throw exception if stuck
        if (terminationConditionMet()) {
            return;  // Successfully converged
        }
    }
}

private void shrinkSimplex(final RealPointValuePair best) {
    double shrinkFactor = 0.5;  // Example shrink factor
    for (int i = 1; i < simplex.length; i++) {
        simplex[i] = performShrink(simplex[i], best, shrinkFactor);
    }
}

private RealPointValuePair performShrink(RealPointValuePair point, RealPointValuePair best, double factor) {
    double[] newPointCoordinates = new double[point.getPointRef().length];
    for (int i = 0; i < point.getPointRef().length; i++) {
        newPointCoordinates[i] = best.getPointRef()[i] + factor * (point.getPointRef()[i] - best.getPointRef()[i]);
    }
    return new RealPointValuePair(newPointCoordinates, evaluateFunction(newPointCoordinates));
}

private boolean terminationConditionMet() {
    // Check if the distance between vertices is smaller than some tolerance, or similar criteria
    return false;  // Placeholder: replace with specific termination logic
}

private void checkIterationsLimit() throws OptimizationException {
    if (getIterations() > getMaxIterations()) {
        throw new OptimizationException("Maximal number of iterations exceeded");
    }
}
