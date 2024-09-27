protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    // If one of the bounds is the exact root, return it.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);

    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    // Ensure to prevent division by zero or very small number which could cause numerical instability
    if (Math.abs(f1 - f0) < Double.MIN_VALUE) {
        return (x0 + x1) / 2;
    }

    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation using the Regula Falsi method.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        // If the new approximation is the exact root, return it.
        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation.
        // Correctly adjust the bounds based on the sign of the function values.
        if (fx * f1 < 0) {
            x0 = x1;
            f0 = f1;
        } else {
            f0 = fx;
        }
        x1 = x;
        f1 = fx;

        // Prevent division by nearly zero difference in subsequent iterations
        if (Math.abs(f1 - f0) < Double.MIN_VALUE) {
            return x1;
        }

        // Check convergence based on function value.
        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        // Check convergence based on interval width.
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }
    }
}