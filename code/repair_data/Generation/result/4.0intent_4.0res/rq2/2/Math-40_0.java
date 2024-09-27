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

        // Ensure that the step size does not fall below precision threshold
        double minStep = FastMath.ulp(FastMath.max(FastMath.abs(xA), FastMath.abs(xB)));
        if ((xB - xA) < minStep) {
            xB = xA + minStep; // Adjust xB slightly to ensure we progress
        }

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

        // target for the next evaluation point
        double targetY;
        if (agingA >= MAXIMAL_AGING) {
            // we keep updating the high bracket, try to compensate this
            targetY = -REDUCTION_FACTOR * yB;
        } else if (agingB >= MAXIMAL_AGING) {
            // we keep updating the low bracket, try to compensate this
            targetY = -REDUCTION_FACTOR * yA;
        } else {
            // bracketing is balanced, try to find the root itself
            targetY = 0;
        }

        // make a few attempts to guess a root,
        double nextX;
        int start = 0;
        int end   = nbPoints;
        do {
            // guess a value for current target, using inverse polynomial interpolation
            System.arraycopy(x, start, tmpX, start, end - start);
            nextX = guessX(targetY, tmpX, y, start, end);

            if (!((nextX > xA) && (nextX < xB))) {
                if (signChangeIndex - start >= end - signChangeIndex) {
                    ++start;
                } else {
                    --end;
                }
                nextX = Double.NaN;
            }
        } while (Double.isNaN(nextX) && (end - start > 1));

        if (Double.isNaN(nextX)) {
            nextX = xA + 0.5 * (xB - xA);
            start = signChangeIndex - 1;
            end   = signChangeIndex;
        }

        final double nextY = computeObjectiveValue(nextX);
        if (Precision.equals(nextY, 0.0, 1)) {
            return nextX;
        }

        if ((nbPoints > 2) && (end - start != nbPoints)) {
            nbPoints = end - start;
            System.arraycopy(x, start, x, 0, nbPoints);
            System.arraycopy(y, start, y, 0, nbPoints);
            signChangeIndex -= start;
        } else if (nbPoints == x.length) {
            nbPoints--;
            if (signChangeIndex >= (x.length + 1) / 2) {
                System.arraycopy(x, 1, x, 0, nbPoints);
                System.arraycopy(y, 1, y, 0, nbPoints);
                --signChangeIndex;
            }
        }

        System.arraycopy(x, signChangeIndex, x, signChangeIndex + 1, nbPoints - signChangeIndex);
        x[signChangeIndex] = nextX;
        System.arraycopy(y, signChangeIndex, y, signChangeIndex + 1, nbPoints - signChangeIndex);
        y[signChangeIndex] = nextY;
        ++nbPoints;

        if (nextY * yA <= 0) {
            xB = nextX;
            yB = nextY;
            absYB = FastMath.abs(yB);
            ++agingA;
            agingB = 0;
        } else {
            xA = nextX;
            yA = nextY;
            absYA = FastMath.abs(yA);
            agingA = 0;
            ++agingB;
            signChangeIndex++;
        }
    }
}