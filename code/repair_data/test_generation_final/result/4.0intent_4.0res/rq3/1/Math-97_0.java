public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign > 0) {
        // If neither yMin nor yMax are close to zero, throw an exception
        throw new IllegalArgumentException
        ("Function values at endpoints do not have different signs. Endpoints: [" + min + "," + max + 
        "], Values: [" + yMin + "," + yMax + "]");
    } else if (sign < 0) {
        // If min and max bracket a root, solve using both endpoints
        ret = solve(min, yMin, max, yMax, min, yMin);
    } else {
        // If sign is exactly zero, check which endpoint is a root
        if (yMin == 0) {
            ret = min;
        } else if (yMax == 0) {
            ret = max;
        }
    }

    return ret;
}