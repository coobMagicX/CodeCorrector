public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    // compute r = sqrt(x^2 + y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);      // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);      // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);                 // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                     // r = sqrt(x^2 + y^2)

    double r = tmp1[0];
    double xValue = x[xOffset];
    double yValue = y[yOffset];

    if (r == 0) {
        // Handle the special case where r is zero, which implies x and y are either both zero or one of them is infinite
        if ((xValue != 0 && Double.isInfinite(xValue)) || (yValue != 0 && Double.isInfinite(yValue))) {
            // The result depends on the sign of infinity and the signs of x and y
            double atan2Result = (Double.isInfinite(xValue) && Double.compare(xValue, 0) > 0)
                                 ? FastMath.PI / 2 : -FastMath.PI / 2;
            for (int i = resultOffset; i < resultOffset + getSize(); ++i) {
                result[i] = atan2Result;
            }
        } else if ((xValue == 0 && yValue > 0) || (xValue == 0 && yValue < 0)) {
            // x is zero, y is non-zero
            double atan2Result = (yValue > 0) ? FastMath.PI / 2 : -FastMath.PI / 2;
            for (int i = resultOffset; i < resultOffset + getSize(); ++i) {
                result[i] = atan2Result;
            }
        } else {
            // Both x and y are zero
            double atan2Result = 0;
            for (int i = resultOffset; i < resultOffset + getSize(); ++i) {
                result[i] = atan2Result;
            }
        }
    } else {
        if (xValue >= 0) {

            // compute atan2(y, x) = 2 atan(y / (r + x))
            add(tmp1, 0, x, xOffset, tmp1, 0);          // r + x
            divide(y, yOffset, tmp1, 0, tmp1, 0);       // y / (r + x)
            atan(tmp1, 0, tmp1, 0);                     // atan(y / (r + x))
            for (int i = resultOffset; i < resultOffset + getSize(); ++i) {
                result[i] = 2 * tmp1[0]; // 2 * atan(y / (r + x))
            }

        } else {

            // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
            subtract(tmp1, 0, x, xOffset, tmp1, 0);     // r - x
            divide(y, yOffset, tmp1, 0, tmp1, 0);       // y / (r - x)
            atan(tmp1, 0, tmp1, 0);                     // atan(y / (r - x))
            result[resultOffset] =
                    ((tmp1[0] <= 0) ? -FastMath.PI : FastMath.PI) - 2 * tmp1[0]; // +/-pi - 2 * atan(y / (r - x))
            for (int i = 1 + resultOffset; i < resultOffset + getSize(); ++i) {
                result[i] = -2 * tmp1[0]; // +/-pi - 2 * atan(y / (r - x))
            }

        }
    }
}