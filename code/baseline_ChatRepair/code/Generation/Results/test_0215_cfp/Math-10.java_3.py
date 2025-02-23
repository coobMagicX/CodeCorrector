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
        result[resultOffset] = 0.0;  // Handle case where r is zero (x = 0 and y = 0), typically atan2(0,0) = 0
        return;
    }

    double sign;
    if (x[xOffset] == 0) {
        sign = (y[yOffset] > 0) ? FastMath.PI / 2 : -FastMath.PI / 2;
    } else if (x[xOffset] < 0) {
        sign = (y[yOffset] >= 0) ? FastMath.PI : -FastMath.PI;
    } else {
        sign = 0;
    }

    // Proceed with the calculation based on x positive or negative
    if (x[xOffset] >= 0) {
        add(tmp1, 0, x, xOffset, tmp2, 0);       // r + x
        divide(y, yOffset, tmp2, 0, tmp1, 0);    // y /(r + x)
        atan(tmp1, 0, tmp2, 0);                  // atan(y / (r + x))
        result[resultOffset] = 2 * tmp2[0];      // 2 * atan(y / (r + x))
    } else {
        subtract(tmp1, 0, x, xOffset, tmp2, 0);  // r - x
        divide(y, yOffset, tmp2, 0, tmp1, 0);    // y /(r - x)
        atan(tmp1, 0, tmp2, 0);                  // atan(y / (r - x))
        result[resultOffset] = sign - 2 * tmp2[0];  // +/-pi - 2 * atan(y / (r - x))
    }
}
