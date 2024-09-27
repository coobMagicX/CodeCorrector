protected double doSolve() {

    // prepare arrays with the first points
    final double[] x = new double[maximalOrder + 1];
    final double[] y = new double[maximalOrder + 1];
    x[0] = getMin();
    x[1] = getStartValue();
    x[2] = getMax();
    verifySequence(x[0], x[1], x[2]);

    // evaluate initial guess
    y[1] = computeObjectiveValue(x[1]);
    if (Precision.equals(y[1], 0.0, 1)) {
        // return the initial guess if it is a perfect root.
        return x[1];
    }

    // evaluate first endpoint
    y[0] = computeObjectiveValue(x[0]);
    if (Precision.equals(y[0], 0.0, 1)) {
        // return the first endpoint if it is a perfect root.
        return x[0];
    }

    int nbPoints;
    int signChangeIndex;
    if (y[0] * y[1] < 0) {

        // reduce interval if it brackets the root
        nbPoints        = 2;
        signChangeIndex = 1;

    } else {

        // evaluate second endpoint
        y[2] = computeObjectiveValue(x[2]);
        if (Precision.equals(y[2], 0.0, 1)) {
            // return the second endpoint if it is a perfect root.
            return x[2];
        }

        if (y[1] * y[2] < 0) {
            // use all computed point as a start sampling array for solving
            nbPoints        = 3;
            signChangeIndex = 2;
        } else {
            throw new NoBracketingException(x[0], x[2], y[0], y[2]);
        }

    }

    // prepare a work array for inverse polynomial interpolation
    final double[] tmpX = new double[x.length];

    // current tightest bracketing of the root
    double xA    = x[signChangeIndex - 1];
    double yA    = y[signChangeIndex - 1];
    double absYA = FastMath.abs(yA);
    int agingA   = 0;
    double xB    = x[signChangeIndex];
    double yB    = y[signChangeIndex];
    double absYB = FastMath.abs(yB);
    int agingB   = 0;

    // search loop
    while (true) {

        // check convergence of bracketing interval
        final double xTol = getAbsoluteAccuracy() +
                            getRelativeAccuracy() * FastMath.max(FastMath.abs(xA), FastMath.abs(xB));
        if (((xB - xA) <= xTol) || (FastMath.max(absYA, absYB) < getFunctionValueAccuracy())) {
            switch (allowed) {
            case ANY_SIDE :
                return absYA < absYB ? xA : xB;
            case LEFT_SIDE :
                return xA;
            case RIGHT_SIDE :
                return xB;
            case BELOW_SIDE :
                return (yA <= 0) ? xA : xB;
            case ABOVE_SIDE :
                return (yA <  0) ? xB : xA;
            default :
                // this should never happen
                throw new MathInternalError();
            }
        }
    }
}