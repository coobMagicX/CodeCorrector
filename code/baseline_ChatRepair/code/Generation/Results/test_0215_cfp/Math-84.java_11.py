protected void iterateSimplex(final Comparator<RealPointValuePair> comparator)
    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

    while (true) {
        incrementIterationsCounter();

        // Save the original vertices
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];

        // Perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            // Reflection yields a better result; check for possible expansion
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // Expansion gives a better result than reflection
                simplex = replaceSimplex(original, expanded);
                continue;
            } else {
                // Accept the reflected simplex because expansion did not improve
                simplex = replaceSimplex(original, reflected);
                continue;
            }
        }

        // Reflection did not yield a better result than the best current point
        if (comparator.compare(reflected, best) >= 0) {
            final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // Contraction yields a better result
                simplex = replaceSimplex(original, contracted);
                continue;
            } else {
                // Contraction failed to improve; use shrink
                simplex = shrinkSimplex(original);
                continue;
            }
        }
        
        // Additional termination criteria can be implemented here if necessary
        break;  // Exit the loop when no improvement is possible or limit reached
    }
}

private RealPointValuePair[] replaceSimplex(RealPointValuePair[] original, RealPointValuePair replacement) {
    // This method replaces the worst performing vertex in the original simplex with the replacement
    // Typically sorts the simplex based on comparator, and replaces the last vertex
    RealPointValuePair[] newSimplex = Arrays.copyOf(original, original.length);
    Arrays.sort(newSimplex, comparator);
    newSimplex[newSimplex.length - 1] = replacement;
    return newSimplex;
}

private RealPointValuePair[] shrinkSimplex(RealPointValuePair[] original) {
    // Shrinks the simplex towards the best point
    RealPointValuePair best = original[0];
    RealPointValuePair[] newSimplex = new RealPointValuePair[original.length];
    for (int i = 0; i < original.length; i++) {
        double[] newPoint = new double[best.getPointRef().length];
        for (int j = 0; j < newPoint.length; j++) {
            newPoint[j] = best.getPointRef()[j] + sigma * (original[i].getPointRef()[j] - best.getPointRef()[j]);
        }
        newSimplex[i] = new RealPointValuePair(newPoint, evaluateFunction(newPoint));
    }
    return newSimplex;
}

private double evaluateFunction(double[] point) {
    // This should be connected to whatever function evaluation logic is appropriate for the optimization
    return 0; 