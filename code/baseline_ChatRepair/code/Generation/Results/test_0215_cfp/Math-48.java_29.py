// Define maximum number of iterations to avoid too many evaluations
int maxIterations = 10000;
int iterations = 0;

while (true) {
    // Increase the iterations counter
    iterations++;

    // Calculate the next approximation.
    final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
    final double fx = computeObjectiveValue(x);

    // Check for convergence by the function value
    if (fx == 0.0 || FastMath.abs(f1) <= ftol) {
        return judgeConvergence(x1, fx, inverted); // returns x1 based on various conditions
    }

    // Update the bounds with the new approximation.
    if (f1 * fx < 0) {
        x0 = x1;
        f0 = f1;
        inverted = !inverted;
    } else {
        // apply method specific behavior here
    }

    x1 = x;
    f1 = fx;

    // Check if interval has sufficiently contracted or if iterations are exhausted
    if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol) || iterations >= maxIterations) {
        return judgeConvergence(x1, fx, inverted); // returns x1 based on various conditions
    }
}

// Additional helper method to judge convergence based on 'allowed' conditions
private double judgeConvergence(double x1, double fx, boolean inverted) {
    switch (allowed) {
        case ANY_SIDE: return x1;
        case LEFT_SIDE: return inverted ? x1 : x0;
        case RIGHT_SIDE: return inverted ? x0 : x1;
        case BELOW_SIDE: return (f1 <= 0) ? x1 : x0;
        case ABOVE_SIDE: return (f1 >= 0) ? x1 : x0;
        default: throw new MathInternalError();
    }
}
