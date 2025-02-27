protected final double doSolve() throws MathIllegalArgumentException {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            default:
                // Valid method was not found, throw an exception
                throw new MathIllegalArgumentException(LocalizedFormats.INVALID_METHOD);
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            switch (allowed) {
            case ANY_SIDE:
                return x1;
            case LEFT_SIDE:
                if (inverted) {
                    return x1;
                }
                break;
            case RIGHT_SIDE:
                if (!inverted) {
                    return x1;
                }
                break;
            case BELOW_SIDE:
                if (f1 <= 0) {
                    return x1;
                }
                break;
            case ABOVE_SIDE:
                if (f1 >= 0) {
                    return x1;
                }
                break;
            default:
                throw new MathInternalError();
            }
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            switch (allowed) {
            case ANY_SIDE:
                return x1;
            case LEFT_SIDE:
                return inverted ? x1 : x0;
            case RIGHT_SIDE:
                return inverted ? x0 : x1;
            case BELOW_SIDE:
                return (f1 <= 0) ? x1 : x0;
            case ABOVE_SIDE:
                return (f1 >= 0) ? x1 : x0;
            default:
                throw new MathInternalError();
            }
        }
    }
}
