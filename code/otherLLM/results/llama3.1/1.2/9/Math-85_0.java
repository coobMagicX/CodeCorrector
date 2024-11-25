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
    
    double a = Math.max(initial - 1.0, lowerBound);
    double b = Math.min(initial + 1.0, upperBound);
    int numIterations = 0;

    do {
        double fa = function.value(a);
        double fb = function.value(b);

        if ((fa * fb > 0.0) && (numIterations < maximumIterations)) {
            if (a > lowerBound) {
                a -= 1.0;
            } else {
                b += 1.0;
            }
        } else if (numIterations >= maximumIterations) {
            throw new ConvergenceException("maximum iterations reached");
        }

        numIterations++;
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations));

    return new double[]{a, b};
}