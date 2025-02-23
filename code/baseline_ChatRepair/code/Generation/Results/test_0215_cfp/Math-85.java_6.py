public static double[] bracket(UnivariateRealFunction function,
                               double initial, double lowerBound, double upperBound, 
                               int maximumIterations) throws ConvergenceException, 
                                                           FunctionEvaluationException {
    
    if (function == null) {
        throw MathRuntimeException.createIllegalArgumentException("function is null");
    }
    if (maximumIterations <= 0) {
        throw MathRuntimeException.createIllegalArgumentException(
            "bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
            "invalid bracketing parameters: lower bound={0}, initial={1}, upper bound={2}",
            lowerBound, initial, upperBound);
    }

    double a = initial;
    double b = initial;
    double fa = function.value(a);
    double fb = function.value(b);
    int numIterations = 0;
    
    // Increase step size based on distance from bounds
    double step = Math.min((upperBound - lowerBound) * 0.01, 1.0); // step size 1% of interval or 1.0

    while ((fa * fb > 0.0) && (numIterations < maximumIterations)) {
        if (a > lowerBound) {
            a = Math.max(a - step, lowerBound);
            fa = function.value(a);
        }
        if (fb * fa > 0.0 && b < upperBound) {
            b = Math.min(b + step, upperBound);
            fb = function.value(b);
        }
        
        numIterations++;
        // Dynamically adjust steps smaller as iterations increase
        if (numIterations % 10 == 0) {
            step *= 0.5; 
        }
    }

    if (fa * fb > 0.0) {
        throw new ConvergenceException(
            "number of iterations={0}, maximum iterations={1}, initial={2}, lower bound={3}, " +
            "upper bound={4}, final a value={5}, final b value={6}, f(a)={7}, f(b)={8}",
            numIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }

    return new double[]{a, b};
}
