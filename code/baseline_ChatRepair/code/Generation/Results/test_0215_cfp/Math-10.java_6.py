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

    // Check for zero and retain result correctly
    if (x[xOffset] == 0 && y[yOffset] == 0) {
        // atan2(0, 0) is defined as 0 in most conventions; change if needed.
        result[resultOffset] = 0.0;
        return;
    }

    if (x[xOffset] >= 0) {
        // compute atan2(y, x) = 2 atan(y / (r + x))
        add(tmp1, 0, x, xOffset, tmp2, 0);
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        for (int i = 0; i < tmp2.length; ++i) {
            result[resultOffset + i] = 2 * tmp2[i];
        }
    } else {
        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        subtract(tmp1, 0, x, xOffset, tmp2, 0);
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        double signMult = y[yOffset] >= 0 ? 1 : -1;
        result[resultOffset] = (signMult * FastMath.PI) - 2 * tmp2[0];
        for (int i = 1; i < tmp2.length; ++i) {
            result[resultOffset + i] = -2 * tmp2[i];
        }
    }

    // Additional checks for floating-point inaccuracies could be added here
}
