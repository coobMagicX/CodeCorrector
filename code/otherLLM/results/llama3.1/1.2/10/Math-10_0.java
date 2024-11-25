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

    double atanResult;
    if (Double.isInfinite(x[xOffset]) || Double.isInfinite(y[yOffset])) {
        result[resultOffset] =
                (x[xOffset] < 0 ? -FastMath.PI : FastMath.PI);
        for (int i = 1; i < result.length; ++i) {
            result[resultOffset + i] = result[resultOffset];
        }
    } else if (Double.isNaN(x[xOffset]) || Double.isNaN(y[yOffset])) {
        for (int i = 0; i < result.length; ++i) {
            result[resultOffset + i] = Double.NaN;
        }
    } else if (x[xOffset] == 0 && y[yOffset] == 0) { // Handle special case for +0/+0
        result[resultOffset] = 0.0;
        for (int i = 1; i < result.length; ++i) {
            result[resultOffset + i] = 0.0;
        }
    } else if ((x[xOffset] == 0 && y[yOffset] < 0) || 
               (x[xOffset] < 0 && y[yOffset] == 0)) { // Correct handling of -0/-0
        atanResult = -FastMath.PI - 2 * atan(y[yOffset]/(x[xOffset]+y[yOffset]));
    } else if ((x[xOffset] == 0 && y[yOffset] > 0) || 
               (x[xOffset] > 0 && y[yOffset] == 0)) {
        atanResult = FastMath.PI - 2 * atan(y[yOffset]/(x[xOffset]+y[yOffset]));
    } else if ((x[xOffset] < 0 && y[yOffset] == 0) || 
               (x[xOffset] > 0 && y[yOffset] < 0)) { // Correct handling of +/-infinity
        atanResult = -FastMath.PI;
    } else {
        if (x[xOffset] >= 0) {

            // compute atan2(y, x) = 2 atan(y / (r + x))
            add(tmp1, 0, x, xOffset, tmp2, 0);          // r + x
            divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r + x)
            atan(tmp1, 0, tmp2, 0);                     // atan(y / (r + x))
            atanResult = 2 * tmp2[0];                    // 2 * atan(y / (r + x))

        } else {

            // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
            subtract(tmp1, 0, x, xOffset, tmp2, 0);     // r - x
            divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r - x)
            atan(tmp1, 0, tmp2, 0);                     // atan(y / (r - x))
            atanResult = -2 * tmp2[0];                   // +/-pi - 2 * atan(y / (r - x))

        }
    }

    result[resultOffset] = atanResult;
    for (int i = 1; i < result.length; ++i) {
        result[resultOffset + i] = atanResult;
    }

}