public double solve(final UnivariateRealFunction f,
            final double min, final double max, final double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {

clearResult();
verifySequence(min, initial, max);

// return the initial guess if it is good enough
double yInitial = f.value(initial);
if (Math.abs(yInitial) <= functionValueAccuracy) {
    setResult(initial, 0);
    return result;
}

// return the first endpoint if it is good enough
double yMin = f.value(min);
if (Math.abs(yMin) <= functionValueAccuracy) {
    setResult(min, 0); // Corrected to setResult(min, 0) instead of setResult(yMin, 0)
    return result;
}

// reduce interval if min and initial bracket the root
if (yInitial * yMin < 0) {
    return solve(f, min, yMin, initial, yInitial, initial, yInitial); // Corrected the order of parameters to match the method signature
}

// return the second endpoint if it is good enough
double yMax = f.value(max);
if (Math.abs(yMax) <= functionValueAccuracy) {
    setResult(max, 0); // Corrected to setResult(max, 0) instead of setResult(yMax, 0)
    return result;
}

// reduce interval if initial and max bracket the root
if (yInitial * yMax < 0) {
    return solve(f, initial, yInitial, max, yMax, max, yMax); // Corrected the order of parameters to match the method signature
}

// full Brent algorithm starting with provided initial guess
return solve(f, min, yMin, max, yMax, initial, yInitial);
}