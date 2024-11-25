public static double[] bracket(UnivariateRealFunction function,
        double initial, double lowerBound, double upperBound, 
        int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
    
    if (function == null) {
        throw MathRuntimeException.createIllegalArgumentException("The provided function is null");
    }
    if (maximumIterations <= 0)  {
        throw MathRuntimeException.createIllegalArgumentException(
              "Bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw MathRuntimeException.createIllegalArgumentException(
              "Invalid bracketing parameters: Lower bound={0}, Initial={1}, Upper bound={2}",
              lowerBound, initial, upperBound);
    }
    double a = initial;
    double b = initial; // Ensuring 'a' and 'b' start at the same value
    double fa;
    double fb;
    int numIterations = 0 ;
    
    do {
        a = Math.max(a - 1.0, lowerBound); // Adjusted to prevent negative values
        b = Math.min(b + 1.0, upperBound); // Adjusted to prevent exceeding the upper bound
        fa = function.value(a);
        
        fb = function.value(b);
        numIterations++ ;
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations) && 
            ((a > lowerBound) || (b < upperBound)));
   
    if (fa * fb >= 0.0 ) {
        throw new ConvergenceException(
              "Number of iterations={0}, Maximum iterations={1}, Initial={2}, Lower bound={3}, Upper bound={4}, Final a value={5}, Final b value={6}, f(a)={7}, f(b)={8}",
              numIterations, maximumIterations, initial,
              lowerBound, upperBound, a, b, fa, fb);
    }
    
    return new double[]{a, b};
}