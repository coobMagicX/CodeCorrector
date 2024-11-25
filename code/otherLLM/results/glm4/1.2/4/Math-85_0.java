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
        a = Math.max(a - 1.0, lowerBound);
        b = Math.min(b + 1.0, upperBound);
        fa = function.value(a);

        // Use solve method to find a bracketing interval
        UnivariateRealFunction g = new BracketingFunctionAdapter(f, a, b);
        double[] solution = solve(g, a, b, 1e-6); // Using a small absolute accuracy for bracketing

        if (solution.length != 2) {
            throw new FunctionEvaluationException("Solution array has unexpected length.");
        }

        a = solution[0];
        b = solution[1];

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

// A helper function to adapt the original function for bracketing
private static class BracketingFunctionAdapter implements UnivariateRealFunction {
    private final UnivariateRealFunction f;
    private final double x0;
    private final double x1;

    public BracketingFunctionAdapter(UnivariateRealFunction f, double x0, double x1) {
        this.f = f;
        this.x0 = x0;
        this.x1 = x1;
    }

    @Override
    public double value(double x) throws FunctionEvaluationException {
        return (x >= x0 && x <= x1) ? f.value(x) : Double.NaN; // Return NaN outside the bracketing interval
    }
}