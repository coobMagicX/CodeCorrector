protected final double doSolve() {
    // All initialization and setup remains the same as the original code...

    double prevX = Double.MAX_VALUE; // Track previous center approximation

    while (true) {
        // Calculate the next approximation as per the original code...
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // Check conditioning of new approximation
        if (FastMath.abs(x - prevX) < atol + rtol * FastMath.abs(x)) {
            if (FastMath.abs(fx) < ftol) {
                return x;
            } else {
                throw new ConvergenceException(LocalizedFormats.CONVERGENCE_FAILED, x);
            }
        }

        prevX = x; // Update the previous approximation

        // All further calculations remain the same...
    }
}
