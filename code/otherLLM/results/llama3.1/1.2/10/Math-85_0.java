public static double[] bracket(UnivariateRealFunction function,
        double initial, double lowerBound, double upperBound, 
        int maximumIterations) throws ConvergenceException, 
        FunctionEvaluationException {
    
    if (function == null) {
        throw MathRuntimeException.createIllegalArgumentException("function is null");
    }
    if (maximumIterations <= 0)  {
        throw MathRuntimeException.createIllegalArgumentException(
              "bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
              "invalid bracketing parameters:  lower bound={0},  initial={1}, upper bound={2}",
              lowerBound, initial, upperBound);
    }
    double a = initial;
    double b = initial;
    double fa;
    double fb;
    int numIterations = 0 ;

    do {
        // Update 'a' and 'b' based on the midpoint
        double midPoint = midpoint(a, b);
        
        if (function.value(midPoint) == 0) {
            a = Math.max(lowerBound, midPoint - 1.0);
            b = Math.min(upperBound, midPoint + 1.0);
        } else if (function.value(midPoint) * function.value(initial) > 0) {
            a = initial;
            b = midPoint;
        } else {
            a = midPoint;
            b = upperBound;
        }
        
        fa = function.value(a);
        fb = function.value(b);
        numIterations++ ;
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations) && 
            ((a > lowerBound) || (b < upperBound)));

    if (fa * fb >= 0.0 ) {
        throw new ConvergenceException(
                  "number of iterations={0}, maximum iterations={1}, " +
                  "initial={2}, lower bound={3}, upper bound={4}, final a value={5}, " +
                  "final b value={6}, f(a)={7}, f(b)={8}",
                  numIterations, maximumIterations, initial,
                  lowerBound, upperBound, a, b, fa, fb);
    }
    
    return new double[]{a, b};
}