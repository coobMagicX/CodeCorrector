public static double[] bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
    if (function == null) {
        throw new IllegalArgumentException("Function is null");
    }
    if (maximumIterations <= 0) {
        throw new IllegalArgumentException("Bad value for maximum iterations number: " + maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw new IllegalArgumentException(
                "Invalid bracketing parameters: lower bound = " + lowerBound + ", initial = " + initial + ", upper bound = " + upperBound);
    }

    double a = initial;
    double b = initial;
    double fa = function.value(a);
    double fb = fa;
    double delta = 0.01;  // Smaller initial step size to boost precision
    int numIterations = 0;

    while ((fa * fb > 0.0) && (numIterations < maximumIterations)) {
        delta = Math.max(delta, 0.01);  // Avoiding too small steps
        if (Math.abs(fa) < Math.abs(fb)) {
            a = Math.max(a - delta, lowerBound);
            fa = function.value(a);
        } else {
            b = Math.min(b + delta, upperBound);
            fb = function.value(b);
        }

        if (a <= lowerBound && b >= upperBound) {
            break;
        }

        delta *= 1.6; // Controlled increment to ensure precision
        numIterations++;
    }

    if (fa * fb > 0.0) {
        throw new ConvergenceException(
                "Unable to bracket the root in the given range. Initial = " + initial +
                ", Lower bound = " + lowerBound + ", Upper bound = " + upperBound +
                ", fa = " + fa + ", fb = " + fb + ", iterations = " + numIterations);
    }
    
    return new double[]{a, b};
}
