public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    
    // Initialize variables for Bisection method
    double a = Math.min(min, initial);
    double b = Math.max(min, initial);
    double c = (a + b) / 2;
    boolean hasInitialGuess = Double.isFinite(initial);

    // Implement the Bisection method logic here
    while (true) {
        // Evaluate function at midpoint
        double fa = f.value(a);
        double fc = f.value(c);

        // Check if we have found a root or are close enough to it
        if (Math.abs(fc) < 1E-6 || Math.abs(b - a) < 1E-6) {
            return c; // Return the approximation of the root
        }

        // Decide the new interval
        if (fa * fc > 0.0) { // f(a) and f(c) have the same sign
            if (!hasInitialGuess || initial < min || initial > max) {
                a = b;
            } else {
                a = c;
            }
        } else { // f(a) and f(c) have different signs
            if (!hasInitialGuess || initial < min || initial > max) {
                b = a;
            } else {
                b = c;
            }
        }

        // Update the midpoint of the interval
        c = (a + b) / 2;
    }
}