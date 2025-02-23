public static double[] bracket(UnivariateRealFunction function,
                               double initial, double lowerBound, double upperBound, 
                               int maximumIterations) throws ConvergenceException, 
                                                           FunctionEvaluationException {
    if (function == null) {
        throw MathRuntimeException.createIllegalArgumentException("function is null");
    }
    if (maximumIterations <= 0) {
        throw MathRuntimeException.createIllegalArgumentException("bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
              "invalid bracketing parameters: lower bound={0}, initial={1}, upper bound={2}",
              lowerBound, initial, upperBound);
    }

    double a = initial;
    double b = initial;
    double fa = function.value(a);
    double fb = fa;
    double stepSize = 1.0;
    boolean aCanMove, bCanMove;
    
    for (int numIterations = 0; numIterations < maximumIterations; ++numIterations) {
        aCanMove = a > lowerBound;
        bCanMove = b < upperBound;
        
        if (!aCanMove && !bCanMove) {
            break;
        }
        
        if (aCanMove) {
            a = Math.max(a - stepSize, lowerBound);
            fa = function.value(a);
        }
        if (bCanMove) {
            b = Math.min(b + stepSize, upperBound);
            fb = function.value(b);
        }
        
        if (fa * fb <= 0) {
            break; 
        }
        
        stepSize *= 2; 
    }

    if (fa * fb > 0) {
        throw new ConvergenceException(
            "number of iterations={0}, maximum iterations={1}, initial={2}, " +
            "lower bound={3}, upper bound={4}, final a value={5}, final b value={6}, f(a)={7}, f(b)={8}",
            numIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }

    
    if (a > b) {
        double temp = a;
        a = b;
        b = temp;
    }

    return new double[] { a, b };
}
