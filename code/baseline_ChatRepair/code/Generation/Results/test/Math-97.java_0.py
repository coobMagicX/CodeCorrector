public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Tolerance for considering a value close enough to zero to count as a root
    double tol = 1e-15; // You may want to adjust this tolerance based on specific functions or requirements

    // Check if either of the endpoint values are close enough to zero to consider them roots
    if (Math.abs(yMin) <= tol) {
        return min;
    } else if (Math.abs(yMax) <= tol) {
        return max;
    }

    // Verify bracketing by checking if the signs are opposite
    if (yMin * yMax >= 0) {
        // Both function values are not close to zero and do not have opposite signs - roots are not bracketed at intervals
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. Endpoints: [" +
            min + ", " + max + "] Values: [" + yMin + ", " + yMax + "]");
    } else {
        // Continue solving since we have verified that a root must exist within (min, max)
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}
