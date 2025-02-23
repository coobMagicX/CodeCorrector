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

    for (int i = 0; i < maximumIterations; ++i) {
        a = Math.max(a - stepSize, lowerBound);
        b = Math.min(b + stepSize, upperBound);
        fa = function.value(a);
        fb = function.value(b);
        if (fa * fb <= 0) {
            break; // If the function evaluations have opposite signs, a root is between a and b
        }
        stepSize *= 2; // Expand search rapidly at the beginning
    }

    if (fa * fb > 0) {
        // After maximum iterations, if still no root bracketing found, throw an exception
        throw new ConvergenceException(
            "number of iterations={0}, maximum iterations={1}, initial={2}, " +
            "lower bound={3}, upper bound={4}, final a value={5}, final b value={6}, f(a)={7}, f(b)={8}",
            maximumIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }
    
    // Ensure [a, b] is ordered correctly
    if (a > b) {
        double temp = a;
        a = b;
        b = temp;
    }

    return new double[] { a, b };
}
