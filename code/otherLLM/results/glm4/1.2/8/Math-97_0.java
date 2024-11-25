public double solve(double min, double max) throws MaxIterationsExceededException,
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // check if either value is close to a zero or exactly zero
        if (Math.abs(yMin) < 1e-8 || Math.abs(yMax) < 1e-8) {
            if (min == max) {
                throw new IllegalArgumentException("Interval width must be greater than zero.");
            }
            // Adjust the interval to exclude the root at one of the endpoints
            if (Math.abs(min - yMin) < 1e-8) {
                min = (min + 0.5 * (max - min));
            } else if (Math.abs(max - yMax) < 1e-8) {
                max = (min + 0.5 * (max - min));
            }
            // Recalculate the values at the new endpoints
            yMin = f.value(min);
            yMax = f.value(max);
        } else {
            throw new IllegalArgumentException("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" +
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}