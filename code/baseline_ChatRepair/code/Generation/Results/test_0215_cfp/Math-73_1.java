public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {

    clearResult();
    verifySequence(min, initial, max);

    
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    
    if (yInitial * yMin < 0) {
        
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    } else if (yInitial * yMax < 0) {
        
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    } else {
        
        throw new IllegalArgumentException("Function values at the endpoints and initial must have opposite signs to guarantee a root exists within the interval: " +
                                           "f(min) = " + yMin + ", f(max) = " + yMax + ", f(initial) = " + yInitial);
    }
}
