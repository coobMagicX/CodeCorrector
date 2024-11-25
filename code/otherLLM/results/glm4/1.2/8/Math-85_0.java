public static double inverseCumulativeProbability(double probability, UnivariateRealFunction function,
        double lowerBound, double upperBound, int maximumIterations) throws ConvergenceException, 
        FunctionEvaluationException {

    setup(function);

    if (probability < 0.0 || probability > 1.0) {
        throw MathRuntimeException.createIllegalArgumentException(
              "invalid probability value: {0}", probability);
    }
    
    double a = lowerBound;
    double b = upperBound;
    double fa = function.value(a);
    double fb = function.value(b);

    if ((fa * fb <= 0.0) || (a >= b)) {
        throw new ConvergenceException(
              "number of iterations={0}, maximum iterations={1}, " +
              "initial={2}, lower bound={3}, upper bound={4}, final a value={5}, " +
              "final b value={6}, f(a)={7}, f(b)={8}",
              0, maximumIterations, a, lowerBound, upperBound, a, b, fa, fb);
    }
    
    double p = (probability - 0.5) / (fb - fa); // Adjust for midpoint method
    double c = 0.5 * (b - a);

    int numIterations = 0;
    while ((numIterations < maximumIterations) && (Math.abs(p - probability) > 1e-8)) {
        if (p <= 0.0) {
            b = a + p * c;
            fb = function.value(b);
        } else {
            a = b - p * c;
            fa = function.value(a);
        }

        numIterations++;
    }

    if (numIterations == maximumIterations) {
        throw new ConvergenceException(
              "number of iterations={0}, maximum iterations={1}, " +
              "initial={2}, lower bound={3}, upper bound={4}, final a value={5}, " +
              "final b value={6}, f(a)={7}, f(b)={8}",
              numIterations, maximumIterations, p * c + a,
              lowerBound, upperBound, a, b, fa, fb);
    }

    return 0.5 * (a + b);
}