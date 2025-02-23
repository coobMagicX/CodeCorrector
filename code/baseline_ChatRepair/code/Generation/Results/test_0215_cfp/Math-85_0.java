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
    double stepSize = 0.5; 

    int iteration = 0;
    while (fa * fb > 0 && iteration < maximumIterations) {
        stepSize *= 2; 
        if (initial - stepSize >= lowerBound) {
            a = initial - stepSize;
            fa = function.value(a);
        }
        if (initial + stepSize <= upperBound) {
            b = initial + stepSize;
            fb = function.value(b);
        }

        iteration++;

        
        if (fa * fb <= 0) {
            break;
        }

        
        if (a == lowerBound && b == upperBound) {
            throw new ConvergenceException(
                "Exhausted search space without finding a bracket: lower bound={0}, upper bound={1}, initial={2}",
                lowerBound, upperBound, initial);
        }
    }

    if (fa * fb > 0) {
        throw new ConvergenceException(
            "Failed to find bracket: iterations={0}, maximum iterations={1}, initial={2}, " +
            "final a value={3}, final b value={4}, f(a)={5}, f(b)={6}",
            iteration, maximumIterations, initial, a, b, fa, fb);
    }

    
    if (a > b) {
        double temp = a;
        a = b;
        b = temp;
    }

    return new double[]{a, b};
}
