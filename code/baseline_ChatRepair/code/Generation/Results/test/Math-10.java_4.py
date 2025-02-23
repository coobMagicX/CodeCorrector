public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    double xVal = x[xOffset];
    double yVal = y[yOffset];

    // Handle special cases
    if (Double.isNaN(xVal) || Double.isNaN(yVal)) {
        result[resultOffset] = Double.NaN;
        return;
    }

    if (xVal == 0.0 && yVal == 0.0) {
        result[resultOffset] = 0.0;
        return;
    }

    if (Double.isInfinite(xVal) || Double.isInfinite(yVal)) {
        if (Double.isInfinite(xVal)) {
            result[resultOffset] = (xVal > 0.0) ? ((yVal >= 0.0) ? 0.0 : Math.PI) : Math.PI;
        } else {
            result[resultOffset] = (yVal > 0.0) ? Math.PI / 2 : -Math.PI / 2;
        }
        return;
    }

    // compute r = sqrt(x^2+y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);      // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);      // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);                 // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                     // r = sqrt(x^2 + y^2)

    if (xVal >= 0) {

        // compute atan2(y, x) = 2 atan(y / (r + x))
        add(tmp1, 0, x, xOffset, tmp2, 0);          // r + x
        divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r + x)
        atan(tmp1, 0, tmp2, 0);                     // atan(y / (r + x))
        for (int i = 0; i < tmp2.length; ++i) {
            result[resultOffset + i] = 2 * tmp2[i]; // 2 * atan(y / (r + x))
        }

    } else {

        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        subtract(tmp1, 0, x, xOffset, tmp2, 0);     // r - x
        divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r - x)
        atan(tmp1, 0, tmp2, 0);                     // atan(y / (r - x))
        result[resultOffset] = ((yVal >= 0.0) ? FastMath.PI : -FastMath.PI) - 2 * tmp2[0];
        for (int i = 1; i < tmp2.length; ++i) {
            result[resultOffset + i] = -2 * tmp2[i];
        }

    }
}
