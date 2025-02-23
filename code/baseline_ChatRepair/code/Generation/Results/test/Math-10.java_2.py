public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);     // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);     // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);                // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                    // r = sqrt(x^2 + y^2)

    if (tmp1[0] == 0) {
        // If r is zero, the angle is undefined but specified by convention to be zero.
        for (int i = 0; i < tmp1.length; ++i) {
            result[resultOffset + i] = 0.0;
        }
        return;
    }

    if (x[xOffset] >= 0) {

        add(tmp1, 0, x, xOffset, tmp2, 0);       // r + x
        divide(y, yOffset, tmp2, 0, tmp1, 0);    // y /(r + x)
        atan(tmp1, 0, tmp2, 0);                  // atan(y / (r + x))
        for (int i = 0; i < tmp2.length; ++i) {
            result[resultOffset + i] = 2 * tmp2[i]; // 2 * atan(y / (r + x))
        }

    } else {

        subtract(tmp1, 0, x, xOffset, tmp2, 0);  // r - x
        divide(y, yOffset, tmp2, 0, tmp1, 0);    // y /(r - x)
        atan(tmp1, 0, tmp2, 0);                  // atan(y / (r - x))
        double signPi = (tmp2[0] <= 0) ? -FastMath.PI : FastMath.PI;
        result[resultOffset] = signPi - 2 * tmp2[0]; // +/-pi - 2 * atan(y / (r - x))
        for (int i = 1; i < tmp2.length; ++i) {
            result[resultOffset + i] = -2 * tmp2[i];
        }
    }
}
