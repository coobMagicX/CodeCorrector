public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    if (yMin * yMax >= 0) {
        // Check if either yMin or yMax is close to zero (approximately zero)
        if (Math.abs(yMin) < functionValueAccuracy || Math.abs(yMax) < functionValueAccuracy) {
            // One of the bounds is a root
            if (Math.abs(yMin) < functionValueAccuracy) {
                ret = min;
            } else {
                ret = max;
            }
            setResult(ret, 0);
            return ret;
        } else {
            // Neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using both endpoints as bracketing values
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}