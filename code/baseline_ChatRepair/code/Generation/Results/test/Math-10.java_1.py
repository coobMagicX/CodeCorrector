public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    // Special case handling for (y == 0) and (x <= 0)
    if (y[yOffset] == 0) {
        if (x[xOffset] < 0) {
            result[resultOffset] = Math.PI;  // atan2(0, -x) for x < 0 is pi
        } else if (x[xOffset] > 0) {
            result[resultOffset] = 0;  // atan2(0, x) for x > 0 is 0
        } else {
            result[resultOffset] = Double.NaN;  // atan2(0, 0) is undefined, typically returns NaN
        }
        return;
    }

    // compute r = sqrt(x^2 + y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);    // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);    // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);               // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                   // r = sqrt(x^2 + y^2)

    if (x[xOffset] >= 0) {
        // compute atan2(y, x) = 2 atan(y / (r + x))
        add(tmp1, 0, x, xOffset, tmp2, 0);        // r + x
        divide(y, yOffset, tmp2, 0, tmp1, 0);     // y / (r + x)
        atan(tmp1, 0, tmp2, 0);                   // atan(y / (r + x))
        for (int i = 0; i < tmp2.length; ++i) {
            result[resultOffset + i] = 2 * tmp2[i];
        }
    } else {
        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        subtract(tmp1, 0, x, xOffset, tmp2, 0);    // r - x
        divide(y, yOffset, tmp2, 0, tmp1, 0);      // y / (r - x)
        atan(tmp1, 0, tmp2, 0);                    // atan(y / (r - x))
        double angleAdjustment = (y[yOffset] > 0) ? Math.PI : -Math.PI;
        result[resultOffset] = angleAdjustment - 2 * tmp2[0];
        for (int i = 1; i < tmp2.length; ++i) {
            result[resultOffset + i] = -2 * tmp2[i];
        }
    }
}
