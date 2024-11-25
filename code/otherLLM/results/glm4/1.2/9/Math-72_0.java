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
    setResult(yMin, 0);
    return result;
}

// reduce interval if min and initial bracket the root
if (yInitial * yMin < 0) {
    double midValue = f.value((min + initial) / 2);
    double newMin = (midValue > 0) ? initial : min;
    double newMax = (midValue > 0) ? min : initial;
    return solve(f, min, newMin, max, yInitial, initial, midValue);
}

// return the second endpoint if it is good enough
double yMax = f.value(max);
if (Math.abs(yMax) <= functionValueAccuracy) {
    setResult(yMax, 0);
    return result;
}

// reduce interval if initial and max bracket the root
if (yInitial * yMax < 0) {
    double midValue = f.value((initial + max) / 2);
    double newMin = (midValue > 0) ? initial : max;
    double newMax = (midValue > 0) ? max : initial;
    return solve(f, min, newMin, newMax, yInitial, max, midValue);
}

if (yMin * yMax > 0) {
    throw MathRuntimeException.createIllegalArgumentException(
          NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
}

// full Brent algorithm starting with provided initial guess
return solve(f, min, (min + initial) / 2, max, (initial + max) / 2,
             initial, yInitial);
}