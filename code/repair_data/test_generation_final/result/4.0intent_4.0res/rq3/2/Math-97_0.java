public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        if (Math.abs(yMin) <= FUNCTION_TOLERANCE || Math.abs(yMax) <= FUNCTION_TOLERANCE) {
            ret = (Math.abs(yMin) <= FUNCTION_TOLERANCE) ? min : max;
        } else {
            // neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException("Function values at endpoints do not have different signs. " +
                    "Endpoints: [" + min + "," + max + "]" +
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // endpoints do bracket a root.
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}