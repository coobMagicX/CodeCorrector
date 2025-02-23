protected final double doSolve() {
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
        final double x2 = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double f2 = computeObjectiveValue(x2);

        if (f2 == 0.0) {
            return x2;
        }

        if (f1 * f2 < 0) {
            x0 = x1;
            f0 = f1;
        } else if (f0 * f2 < 0) {
            inverted = !inverted;
        } else {
            throw new NoBracketingException(x0, x1, f0, f1);
        }

        x1 = x2;
        f1 = f2;

        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return inverted ? x0 : x1;
        }

        if(inverted){
            double temp = x0;
            x0 = x1;
            x1 = temp;
            temp = f0;
            f0 = f1;
            f1 = temp;
        }
    }
}
