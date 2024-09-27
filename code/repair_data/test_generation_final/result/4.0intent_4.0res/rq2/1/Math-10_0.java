public void atan2(final double[] y, final int yOffset,
          final double[] x, final int xOffset,
          final double[] result, final int resultOffset) {

    // compute r = sqrt(x^2+y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);      // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);      // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);                 // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                     // r = sqrt(x^2 + y^2)

    if (x[xOffset] >= 0) {

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
        double piAdjustment = (y[yOffset] >= 0) ? FastMath.PI : -FastMath.PI;
        for (int i = 0; i < tmp2.length; ++i) {
            result[resultOffset + i] = piAdjustment - 2 * tmp2[i]; // +/-pi - 2 * atan(y / (r - x))
        }
    }

    // Handling special cases like (+0/+0, +0/-0, -0/+0, -0/-0, +/-infinity)
    if (y[yOffset] == 0.0 && x[xOffset] == 0.0) {
        result[resultOffset] = (y[yOffset] >= 0.0 ? 0.0 : -0.0);
    } else if (Double.isInfinite(y[yOffset]) || Double.isInfinite(x[xOffset])) {
        result[resultOffset] = FastMath.PI / 2 * Math.signum(y[yOffset]);
    }
}