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
        return solveDetailed(f, min, yMin, initial, yInitial);
    }

    
    if (yInitial * yMax < 0) {
        return solveDetailed(f, initial, yInitial, max, yMax);
    }
    
    
    throw new IllegalArgumentException("Function values at the endpoints do not bracket the root: " +
                                       "f(min)=" + yMin + ", f(max)=" + yMax + ".");
}

private double solveDetailed(final UnivariateRealFunction f, 
                             double a, double fa, double b, double fb) {
    
    
    