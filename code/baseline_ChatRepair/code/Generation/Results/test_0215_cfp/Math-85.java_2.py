public static double[] bracket(UnivariateRealFunction function,
                               double initial, double lowerBound, double upperBound, 
                               int maximumIterations) throws ConvergenceException,
                               FunctionEvaluationException {

    if (function == null) {
        throw MathRuntimeException.createIllegalArgumentException("Function is null");
    }
    if (maximumIterations <= 0) {
        throw MathRuntimeException.createIllegalArgumentException(
            "Bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
            "Invalid bracketing parameters: lower bound={0}, initial={1}, upper bound={2}",
            lowerBound, initial, upperBound);
    }

    double a = initial;
    double b = initial;
    double fa = function.value(a);
    double fb = function.value(b);
    double delta = (upperBound - lowerBound) / 100.0;  // Smaller initial delta could be more appropriate

    int numIterations = 0;
    while (fa * fb > 0.0 && numIterations < maximumIterations) {
        if (a > lowerBound) {
            a = Math.max(a - delta, lowerBound);
            fa = function.value(a);
        }
        if (fb * fa <= 0.0) {
            break;
        }
        if (b < upperBound) {
            b = Math.min(b + delta, upperBound);
            fb = function.value(b);
        }
        if (fa * fb <= 0.0) {
            break;
        }
        delta *= 2; // Increment the range exponentially
        numIterations++;
    }

    // Refinement phase for increased precision
    if (fa * fb > 0.0) {
        delta = (b - a) / 100.0; // Reset to a precise delta based on current bracket size
        for (int i = 0; fa * fb > 0.0 && i < 100; i++) {
            a = Math.max(a - delta, lowerBound);
            fa = function.value(a);
            b = Math.min(b + delta, upperBound);
            fb = function.value(b);
        }
    }

    if (fa * fb > 0.0) {
        throw new ConvergenceException(
            "Failed to bracket the root, iterations={0}, initial={1}, a={2}, b={3}, fa={4}, fb={5}",
            numIterations, initial, a, b, fa, fb);
    }

    return new double[] {a, b};
}
