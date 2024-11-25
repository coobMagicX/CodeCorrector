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

    double xValue = x[xOffset];
    double yValue = y[yOffset];

    if (xValue == 0) {
        if (yValue > 0) {
            for (int i = 0; i < tmp1.length; ++i) {
                result[resultOffset + i] = FastMath.PI / 2;
            }
        } else if (yValue < 0) {
            for (int i = 0; i < tmp1.length; ++i) {
                result[resultOffset + i] = -FastMath.PI / 2;
            }
        } else {
            // Both x and y are zero, atan2(y,x) is undefined. Set to NaN or a default value.
            for (int i = 0; i < tmp1.length; ++i) {
                result[resultOffset + i] = Double.NaN;
            }
        }
    } else if (xValue != 0) {
        double rPlusX = r + xValue;
        double rMinusX = r - xValue;

        if (xValue >= 0) {

            // compute atan2(y, x) = 2 atan(y / (r + x))
            divide(y, yOffset, tmp1, 0, tmp1, 0);       // y / (r + x)
            atan(tmp1, 0, tmp1, 0);                     // atan(y / (r + x))
            for (int i = 0; i < tmp1.length; ++i) {
                result[resultOffset + i] = 2 * tmp1[i]; // 2 * atan(y / (r + x))
            }

        } else {

            // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
            divide(y, yOffset, tmp2, 0, tmp1, 0);       // y / (r - x)
            atan(tmp1, 0, tmp1, 0);                     // atan(y / (r - x))
            for (int i = 0; i < tmp1.length; ++i) {
                result[resultOffset + i] = FastMath.PI - 2 * tmp1[i]; // pi - 2 * atan(y / (r - x))
            }
        }
    }
}