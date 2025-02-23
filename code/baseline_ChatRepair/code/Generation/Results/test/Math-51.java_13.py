protected final double doSolve() {
    // existing setup code

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Update the bounds with the new approximation
        if (f1 * fx < 0) {
            // The value of x1 has switched to the other bound, thus inverting
            // the interval.
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // Provide comments if f0 modification strategies here as in the original code
        }

        x1 = x;
        f1 = fx;

        // Check if the function value is close enough to zero
        if (FastMath.abs(f1) <= ftol) {
            return handleAllowedSolutions(inverted, x1, x0, f1);
        }

        // Check the interval width, with respect to absolute and relative tolerance
        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return handleAllowedSolutions(inverted, x1, x0, f1);
        }

        // Ensure the method does not loop indefinitely by adding a maximum number of iterations
        if (incrementEvaluationCount() > maxEvaluations) {
            throw new TooManyEvaluationsException(maxEvaluations);
        }
    }
}

private double handleAllowedSolutions(boolean inverted, double x1, double x0, double f1) {
    // fully implement cases depending on 'allowed' with appropriate logic
    // extend cases to manage each situation: ANY_SIDE, LEFT_SIDE, RIGHT_SIDE, BELOW_SIDE, ABOVE_SIDE
    // throw new MathInternalError() in the default case which should never happen
    // this refactoring is for better organization and management of the decision-making, similar to the given original method
}

// it's assumed there's a method like 'incrementEvaluationCount()' tracking counts
