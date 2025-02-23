public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    
    double yInitial = f.value(initial);
    double yMin = f.value(min);
    double yMax = f.value(max);

    
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return initial;  
    }

    
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return min;  
    }

    
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return max;  
    }

    
    if (yInitial * yMin < 0) {
        
        return solve(f, min, initial, initial);
    }

    
    if (yInitial * yMax < 0) {
        
        return solve(f, initial, max, initial);
    }

    
    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    
    
    
    
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}
