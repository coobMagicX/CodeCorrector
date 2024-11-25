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
    setResult(min, 0);
    return result;
}

// return the second endpoint if it is good enough
double yMax = f.value(max);
if (Math.abs(yMax) <= functionValueAccuracy) {
    setResult(max, 0);
    return result;
}

// reduce interval if min and initial bracket the root
if (yInitial * yMin < 0) {
    // Use a new method that reduces the interval correctly
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}

// reduce interval if initial and max bracket the root
if (yInitial * yMax < 0) {
    // Use a new method that reduces the interval correctly
    return solve(f, initial, yInitial, max, yMax, min, yMin);
}

if (yMin * yMax > 0) {
    throw MathRuntimeException.createIllegalArgumentException(
          NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
}

// full Brent algorithm starting with provided initial guess
return solve(f, min, f.value(min), max, f.value(max), initial, f.value(initial));
}