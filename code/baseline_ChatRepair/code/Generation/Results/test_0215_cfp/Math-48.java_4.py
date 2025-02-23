protected final double doSolve() throws ConvergenceException {
    // Get initial solution bounds and objective values
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If initial bounds directly solve the equation, return the solution
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1); // ensure x0 and x1 bracket a root

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;
    int iterationCount = 0; // Count iterations to handle loops effectively

    while (true) {
        if (iterationCount > getMaxEvaluations()) {
            throw new ConvergenceException(); // Convergence failed
        }

        // Compute next approximation and function value
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Interval update logic
        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            adjustIntervalBound(f0, f1, fx); // Pseudocode, replace as needed
        }

        x1 = x;
        f1 = fx;

        // Function value is sufficiently small
        if (FastMath.abs(f1) <= ftol) {
            return x1;
        }

        // Current approximation check for accuracy and interval size
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return handleApproximationAllowed(x1, x0, f1, inverted);
        }

        iterationCount++; // Iteration counter update
    }
}

private void adjustIntervalBound(double& f0, double f1, double fx) {
   // Adjust interval bound based on specified method (e.g., ILLINOIS, PEGASUS, REGULA_FALSI)
   // Implement specific adjustments here
}

private double handleApproximationAllowed(double x1, double x0, double f1, boolean inverted) {
   // Handle the exact conditions based on the allowed method
   // Return either x0 or x1 based on conditions checked against `allowed`
   // Pseudocode, implement the actual handling based on configuration
}
