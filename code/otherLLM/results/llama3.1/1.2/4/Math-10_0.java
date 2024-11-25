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
        if (tmp1[0] == Double.POSITIVE_INFINITY && tmp2[0] > 0) {
            for (int i = 0; i < result.length; ++i) {
                result[resultOffset + i] = FastMath.PI;
            }
        } else {
            atan(tmp1, 0, tmp2, 0);                     // atan(y / (r + x))
            compose(tmp2, 0, new double[] { 2.0 }, result, resultOffset);
        }

    } else if (x[xOffset] < 0) {

        // compute atan2(y, x) = -atan(x / y)
        add(x, xOffset, y, yOffset, tmp1, 0);          // x + y
        divide(x, xOffset, tmp1, 0, tmp2, 0);       // x /(x+y)
        if (tmp2[0] == Double.POSITIVE_INFINITY && x[xOffset] < 0) {
            for (int i = 0; i < result.length; ++i) {
                result[resultOffset + i] = -FastMath.PI;
            }
        } else {
            atan(tmp1, 0, tmp2, 0);                     // atan(x / y)
            compose(tmp2, 0, new double[] { -1.0 }, result, resultOffset);
        }

    } else {

        // compute atan2(y, x) = atan(0 / (x + y))
        for (int i = 0; i < result.length; ++i) {
            result[resultOffset + i] = 0;
        }
    }
}