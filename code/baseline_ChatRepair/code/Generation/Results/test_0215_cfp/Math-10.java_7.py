public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {

    final double EPSILON = 1.0e-15; // Small threshold to handle floating-point errors.

    // Compute r = sqrt(x^2 + y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);      // x^2
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);      // y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);                 // x^2 + y^2
    rootN(tmp2, 0, 2, tmp1, 0);                     // r = sqrt(x^2 + y^2)

    boolean xIsZero = Math.abs(x[xOffset]) < EPSILON;
    boolean yIsZero = Math.abs(y[yOffset]) < EPSILON;

    // Handle special cases for atan2(0, 0), atan2(±y, 0), atan2(0, ±x)
    if (xIsZero && yIsZero) {
        result[resultOffset] = 0.0;               // Conventionally, atan2(0, 0) is 0.
        return;
    } else if (xIsZero) {
        result[resultOffset] = y[yOffset] > 0 ? FastMath.PI/2 : -FastMath.PI/2;
        return;
    } else if (yIsZero) {
        result[resultOffset] = x[xOffset] > 0 ? 0 : FastMath.PI;
        return;
    }

    if (x[xOffset] > 0) {
        // compute atan2(y, x) = 2 * atan(y / (r + x))
        add(tmp1, 0, x, xOffset, tmp2, 0);
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        result[resultOffset] = 2 * tmp2[0];
    } else {
        // compute atan2(y, x) = pi +/- 2 * atan(y / (x - r))
        subtract(tmp1, 0, x, xOffset, tmp2, 0);
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        result[resultOffset] = (y[yOffset] > 0 ? FastMath.PI : -FastMath.PI) - 2 * tmp2[0];
    }

    // Normalize the result to ensure it's within the range (-π, π]
    if (result[resultOffset] > FastMath.PI) {
        result[resultOffset] -= 2 * FastMath.PI;
    } else if (result[resultOffset] <= -FastMath.PI) {
        result[resultOffset] += 2 * FastMath.PI;
    }
}
