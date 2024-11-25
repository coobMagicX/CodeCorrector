public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing and check if either value is close to a zero
    double sign = yMin * yMax;
    if (sign >= 0) {
        // neither value is close to zero and min and max do not bracket root.
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. " +
                "Endpoints: [" + min + "," + max + "] Values: [" + yMin + "," + yMax + "]");
    } else {
        // Solve for both possible roots (one or both endpoints could be a root)
        double rootMin = isZeroClose(min, f.value(min)) ? min : Double.NaN;
        double rootMax = isZeroClose(max, f.value(max)) ? max : Double.NaN;
        
        if (!Double.isNaN(rootMin) && !Double.isNaN(rootMax)) {
            // Both endpoints are roots, choose one
            return Math.min(rootMin, rootMax);
        } else if (!Double.isNaN(rootMin)) {
            // Only min is a root
            return rootMin;
        } else if (!Double.isNaN(rootMax)) {
            // Only max is a root
            return rootMax;
        }
    }

    throw new MaxIterationsExceededException("No root found in the interval [" + min + ", " + max + "]");
}

private boolean isZeroClose(double x, double y) {
    final double tolerance = 1e-10; // Define an appropriate tolerance for your context
    return Math.abs(x - y) < tolerance;
}