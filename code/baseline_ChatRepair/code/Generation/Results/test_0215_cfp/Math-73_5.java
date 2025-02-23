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

    
    boolean minInitialBracket = (yMin * yInitial < 0);
    boolean initialMaxBracket = (yInitial * yMax < 0);

    
    if (minInitialBracket) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    
    if (initialMaxBracket) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    
    if (!minInitialBracket && !initialMaxBracket) {
        throw new IllegalArgumentException("The function does not have opposite signs at the endpoints [" + min + ", " + max +
                                           "]. Unable to find a bracketing interval where f(min) = " + yMin + 
                                           " and f(max) = " + yMax);
   