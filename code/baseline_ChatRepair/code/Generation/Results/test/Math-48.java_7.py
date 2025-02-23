protected final double doSolve() {
    ...
    // Setup a counter or a safeguard against excessive evaluations
    int maxIterations = getMaxEvaluations(); // Make sure there is a sensible max evaluations limit.
    int iterationCount = 0;

    while (iterationCount < maxIterations) {
        iterationCount++; // Increment iteration counter at each iteration

        ...
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        
        // Check progress
        if (Math.abs(x1 - x) < atol || Math.abs(fx) < ftol) {
            return checkAllowedSolution(x, fx, inverted);
        }

        // Update bounds methodically
        boolean signChange = f1 * fx < 0;
        if (signChange) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        }

        x1 = x; // Always bracket the range closer to the root
        f1 = fx;

        // Evaluate if the updated interval is small enough
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1; // Return if changes are below both tolerances
        }

        // Specific method adjustments
        if (!signChange) {
            applyMethodAdjustments(f0, f1, fx, method);
        }
    }
    
    throw new TooManyEvaluationsException(maxIterations); // Exception if max iterations are exceeded
}

private void applyMethodAdjustments(...) {
    switch (method) {
        case ILLINOIS:
            f0 *= 0.5;
            break;
        case PEGASUS:
            f0 *= f1 / (f1 + fx);
            break;
        case REGULA_FALSI:
            // Define more adequately how to handle stuck situations
            f0 = adjustForRegulaFalsi(f0, f1, fx);
            break;
        default:
            throw new MathInternalError();
    }
}

private double adjustForRegulaFalsi(double f0, double f1, double fx) {
    // Implementation should act to prevent convergence blocking
    // This is a simplistic approach, consider elaborating further
    if (f1 * fx > 0) {
        f1 *= 0.5;
    }
    return f0;
}

private double checkAllowedSolution(double x, double fx, boolean inverted) {
    // Depending on `allowed`, decide whether `x` is acceptable, similar
    // to checks already found in your existing code.
    ...
}
