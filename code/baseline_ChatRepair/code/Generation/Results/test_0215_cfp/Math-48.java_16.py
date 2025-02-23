protected final double doSolve() {
    // Define a maximum number of iterations
    final int maxIterations = 100; // you can adjust according to acceptable application behavior
    int iterationCount = 0;

    ...

    while (true) {
        iterationCount++;
        if (iterationCount > maxIterations) {
            throw new ConvergenceException(LocalizedFormats.FAILED_CONVERGENCE, maxIterations);
        }

        ...

        // Current implementation
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        ...

        // Update bounds logic
        if (FastMath.abs(f1) <= ftol) {
            ...
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            ...
        }
    }
}
