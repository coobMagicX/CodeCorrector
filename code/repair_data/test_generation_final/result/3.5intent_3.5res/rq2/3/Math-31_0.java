if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
    double scaleFactor = 1d;
    double lastScaleFactor = 1d;
    final int maxPower = 5;
    final double scale = FastMath.max(a,b);
    if (scale <= 0) {  // Can't scale
        throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
    }
    for (int i = 0; i < maxPower; i++) {
        lastScaleFactor = scaleFactor;
        scaleFactor *= scale;
        if (a != 0.0 && a > b) {
            cN = hPrev / lastScaleFactor + (b / scaleFactor * p0);
            q2 = q1 / lastScaleFactor + (b / scaleFactor * dPrev);
        } else if (b != 0) {
            cN = (a / scaleFactor * hPrev) + p0 / lastScaleFactor;
            q2 = (a / scaleFactor * q1) + dPrev / lastScaleFactor;
        }
        if (!(Double.isInfinite(cN) || Double.isInfinite(q2))) {
            break;
        }
    }
}