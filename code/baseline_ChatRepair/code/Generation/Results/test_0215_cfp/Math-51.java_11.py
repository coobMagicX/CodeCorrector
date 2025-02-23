protected final double doSolve() {
    final double functionValueAccuracy = getFunctionValueAccuracy();

    // Initialization
    double lo = getMin();
    double hi = getMax();
    double fLo = computeObjectiveValue(lo);
    double fHi = computeObjectiveValue(hi);

    // Initial checks for immediate solutions
    if (fLo == 0.0) {
        return lo;
    }
    if (fHi == 0.0) {
        return hi;
    }

    // Ensure bracketing
    verifyBracketing(lo, hi);

    // Setup for main algorithm
    while (true) {
        // Bisection as the next guess point
        double x = 0.5 * (lo + hi);
        double fx = computeObjectiveValue(x);

        if (Math.abs(fx) <= functionValueAccuracy) {
            return x;
        }

        // Update bounds
        if (Math.signum(fx) != Math.signum(fLo)) {
            hi = x;
            fHi = fx;
        } else {
            lo = x;
            fLo = fx;
        }

        // Convergence check based on function values
        double diff = Math.abs(hi - lo);
        if (diff <= Math.max(getAbsoluteAccuracy(), getRelativeAccuracy() * Math.abs(x))) {
            return x;
        }

        // Additional checks to handle specific requirements (i.e., if only one side of the root is allowed)
        switch (getAllowed()) {
            case ANY_SIDE:
                break;
            case LEFT_SIDE:
                if (fx > 0) { lo = x + getAbsoluteAccuracy(); }
                break;
            case RIGHT_SIDE:
                if (fx < 0) { hi = x - getAbsoluteAccuracy(); }
                break;
            case BELOW_SIDE:
                if (fx > 0) { hi = x; fHi = fx; } // Approach from below
                break;
            case ABOVE_SIDE:
                if (fx < 0) { lo = x; fLo = fx; } // Approach from above
                break;
            default:
                throw new MathInternalError();
        }
    }
}
