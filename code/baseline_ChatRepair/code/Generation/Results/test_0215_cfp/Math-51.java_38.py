protected final double doSolve() {
    // Initialization and existing code remains the same

    // Improved handling within the primary iteration loop:
    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0 || FastMath.abs(fx) <= ftol) {
            return x;
        }

        // Update the interval, ensuring progress towards convergence
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (FastMath.abs(x1 - x) < atol || FastMath.abs(f1 - fx) < ftol) {
                return x;  // Accept the approximation if within tolerances
            }
        }

        // Update upper bound always to ensure convergence
        x1 = x;
        f1 = fx;

        // Stopping criteria: Check the relative size of the interval
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1; // Return current approximation if sufficiently close
        }
    }
}
