public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // Check for a root at either endpoint by evaluating the function at midpoint and checking its sign with respect to an arbitrary point (e.g., 0)
        double midpoint = (min + max) / 2.0;
        double fMidpoint = f.value(midpoint);
        if (Math.abs(fMidpoint) < tolerance) { // Assuming 'tolerance' is defined and used for checking near-zero values
            throw new IllegalArgumentException("A root is found at the endpoints: [" + min + "," + max + "]");
        }
        // neither value is close to zero and min and max do not bracket root.
        throw new IllegalArgumentException(
                "Function values at endpoints do not have different signs. " +
                "Endpoints: [" + min + "," + max + "]" +
                "Values: [" + yMin + "," + yMax + "]");
    } else {
        // Check if either endpoint is a root
        boolean isRootAtMin = Math.abs(yMin) < tolerance;
        boolean isRootAtMax = Math.abs(yMax) < tolerance;

        if (isRootAtMin && isRootAtMax) {
            // Both endpoints are roots, return either of them as the result (or throw an exception depending on requirements)
            ret = min; // or max could be used
        } else if (isRootAtMin || isRootAtMax) {
            // Only one endpoint is a root, use it directly
            ret = isRootAtMin ? min : max;
        } else {
            // No roots at endpoints, solve using only the first endpoint as initial guess
            ret = solve(min, yMin, max, yMax, min, yMin);
        }
    }

    return ret;
}