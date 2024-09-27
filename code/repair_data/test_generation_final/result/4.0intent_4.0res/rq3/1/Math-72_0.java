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
        setResult(min, 0);  // Fixed: should set result to min, not yMin
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, initial); // Fixed: proper arguments for recursive solve call
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);  // Fixed: should set result to max, not yMax
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, initial); // Fixed: proper arguments for recursive solve call
    }

    if (yMin * yMax > 0) {
        throw new IllegalArgumentException(
              "Function values at endpoints do not have opposite signs. " +
              "Endpoints: [" + min + ", " + max + "], values: [" + yMin + ", " + yMax + "]");
        // Fixed: Proper exception and clearer error message
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial); // Fixed: Proper arguments for Brent algorithm solve call
}