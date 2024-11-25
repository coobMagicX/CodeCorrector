public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    // Handle special cases for +0/+0, -0/-0, and +/-infinity before proceeding with general case computation.
    if (Double.isNaN(x[xOffset]) && Double.isNaN(y[yOffset])) {
        // Both x and y are NaN, atan2 is NaN
        for (int i = 0; i < getSize(); ++i) {
            result[resultOffset + i] = Double.NaN;
        }
        return;
    }

    if (Double.isInfinite(x[xOffset]) && Double.isInfinite(y[yOffset])) {
        // Both x and y are infinite
        if (x[xOffset] == y[yOffset]) {
            // atan2(infinity, infinity) is undefined; treat as NaN for consistency with double behavior
            for (int i = 0; i < getSize(); ++i) {
                result[resultOffset + i] = Double.NaN;
            }
        } else {
            // atan2(infinity, infinity) can be any real number, let's use 0 for simplicity
            for (int i = 0; i < getSize(); ++i) {
                result[resultOffset + i] = 0.0;
            }
        }
        return;
    }

    if (x[xOffset] == 0 && y[yOffset] == 0) {
        // atan2(0, 0) is undefined; treat as NaN for consistency with double behavior
        for (int i = 0; i < getSize(); ++i) {
            result[resultOffset + i] = Double.NaN;
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

    if (x[xOffset] >= 0) {

        // compute atan2(y, x) = 2 atan(y / (r + x))
        add(tmp1, 0, x, xOffset, tmp2, 0);          // r + x
        divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r + x)
        atan(tmp1, 0, result, resultOffset);        // atan(y / (r + x))
        for (int i = 0; i < getSize(); ++i) {
            result[resultOffset + i] *= 2.0;         // 2 * atan(y / (r + x))
        }

    } else {

        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        subtract(tmp1, 0, x, xOffset, tmp2, 0);     // r - x
        divide(y, yOffset, tmp2, 0, tmp1, 0);       // y /(r - x)
        atan(tmp1, 0, result, resultOffset);        // atan(y / (r - x))
        double sign = ((tmp1[0] <= 0) ? -1.0 : 1.0);
        for (int i = 0; i < getSize(); ++i) {
            result[resultOffset + i] *= sign * 2.0; // +/-pi - 2 * atan(y / (r - x))
        }

    }
}