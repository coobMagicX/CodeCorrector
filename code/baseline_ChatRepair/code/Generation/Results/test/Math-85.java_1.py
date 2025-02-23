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
            "invalid bracketing parameters: lower-bound={0}, initial={1}, upper-bound={2}",
            lowerBound, initial, upperBound);
    }

    double a = initial;
    double b = initial;
    double fa = function.value(a);
    double fb = function.value(b);
    double delta = 0.5; // Adjust this delta if necessary for the scale of input values

    if (fa * fb <= 0) {
        return new double[] { a, b };
    }

    int numIterations = 0;
    while (fa * fb > 0 && numIterations < maximumIterations) {
        if (a != lowerBound) {
            a = Math.max(a - delta, lowerBound);
            fa = function.value(a);
        }
        if (fa * fb <= 0) {
            return new double[] { a, b };
        }
        if (b != upperBound) {
            b = Math.min(b + delta, upperBound);
            fb = function.value(b);
        }
        delta = Math.min(delta * 2, upperBound - lowerBound);

        numIterations++;
    }

    if (fa * fb >= 0.0) {
        throw new ConvergenceException(
            "number of iterations={0}, maximum iterations={1}, initial={2}, lower-bound={3}, upper-bound={4}, final a={5}, final b={6}, f(a)={7}, f(b)={8}",
            numIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }

    return new double[] { a, b };
}
