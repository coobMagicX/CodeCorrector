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

    double r = tmp1[0]; // Extract the computed radius

    if (Double.isNaN(r) || Double.isInfinite(r)) {
        // Handle special cases of infinity and NaN
        if (x[xOffset] == 0 && y[yOffset] == 0) {
            // Both x and y are zero, atan2 should return 0
            result[resultOffset] = 0;
        } else {
            // If r is infinite and both x and y are zero, atan2 should return NaN
            result[resultOffset] = Double.NaN;
        }
    } else if (x[xOffset] == 0) {
        // Special cases when x is zero
        if (y[yOffset] > 0) {
            result[resultOffset] = FastMath.PI / 2;
        } else if (y[yOffset] < 0) {
            result[resultOffset] = -FastMath.PI / 2;
        }
    } else if (y[yOffset] == 0) {
        // Special case when y is zero
        result[resultOffset] = x[xOffset] > 0 ? 0 : FastMath.PI;
    } else {
        // Normal computation for non-zero x and y

        double ratio;

        if (x[xOffset] >= 0) {
            // Compute atan2(y, x) = 2 atan(y / (r + x))
            add(tmp1, 0, x, xOffset, tmp1, 0);          // r + x
            divide(y, yOffset, tmp1, 0, tmp1, 0);       // y /(r + x)
            ratio = atan(tmp1, 0);
        } else {
            // Compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
            subtract(tmp1, 0, x, xOffset, tmp1, 0);     // r - x
            divide(y, yOffset, tmp1, 0, tmp1, 0);       // y /(r - x)
            ratio = atan(tmp1, 0);
            if (x[xOffset] < 0) {
                ratio -= FastMath.PI;
            }
        }

        result[resultOffset] = 2 * ratio; // Final value
    }

    // Fill the rest of the array with zeros
    for (int i = 1; i < getSize(); ++i) {
        result[resultOffset + i] = 0;
    }
}