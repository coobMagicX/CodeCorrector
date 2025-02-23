public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {

    clearResult();
    verifySequence(min, initial, max);
    
    
    double yInitial = f.value(initial);
    double yMin = f.value(min);
    double yMax = f.value(max);

    
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }
    
    
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }
    
    
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }
    
    
    
    throw new IllegalArgumentException("No root bracketing found: f(min)=" + yMin + ", f(initial)=" + yInitial + ", f(max)=" + yMax);
}
