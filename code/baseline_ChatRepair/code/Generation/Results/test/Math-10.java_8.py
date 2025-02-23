public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    // First check the special cases explicitly
    if (isZero(y, yOffset) && isZero(x, xOffset)) {
        // atan2(0,0) is undefined; could choose to return 0 for this implementation
        result[resultOffset] = 0;
        return;
    }

    // Edge case handling: y-axis symmetry cases, Inf, -Inf etc.
    if (Double.isInfinite(y[yOffset]) || Double.isInfinite(x[xOffset])) {
        result[resultOffset] = handleInfinite(y, yOffset, x, xOffset);
        return; 
    }

    // Normal cases
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);      // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);      // y^2
    add(tmp1, 0, tmp2, 0, tmp1, 0);                 // x^2 + y^2
    rootN(tmp1, 0, 2, tmp1, 0);                     // r = sqrt(x^2 + y^2)

    if (x[xOffset] >= 0) {
        add(tmp1, 0, x, xOffset, tmp2, 0);          // r + x
    } else {
        subtract(tmp1, 0, x, xOffset, tmp2, 0);     // r - x
    }

    if (isZero(tmp2, 0)) {  // Avoid division by zero
        result[resultOffset] = x[xOffset] >= 0 ? 0 : FastMath.PI;
    } else {
        divide(y, yOffset, tmp2, 0, tmp1, 0);       // y / (r ± x)
        atan(tmp1, 0, tmp1, 0);                     // atan(y / (r ± x))
        result[resultOffset] = x[xOffset] >= 0 ? 2 * tmp1[0] : FastMath.PI - 2 * tmp1[0];
    }
}

private boolean isZero(double[] arr, int offset) {
    return arr[offset] == 0;
}

private double handleInfinite(double[] y, int yOffset, double[] x, int xOffset) {
    if (Double.isInfinite(y[yOffset]) && Double.isInfinite(x[xOffset])) {
        return FastMath.PI * 0.25 * Math.signum(y[yOffset]) * (1 + Math.signum(x[xOffset]));
    } else if (Double.isInfinite(x[xOffset])) {
        return Math.signum(x[xOffset]) * FastMath.PI;
    } else { // y is infinite
        return FastMath.PI * 0.5 * Math.signum(y[yOffset]);
    }
}
