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

    double signX = (x[xOffset] > 0) ? 1.0 : -1.0;
    double signY = (y[yOffset] > 0) ? 1.0 : -1.0;

    if (signX == 0 && signY == 0) {
        // (+0/+0, +0/-0, -0/+0, -0/-0): atan2(0, 0) is 0
        result[resultOffset] = 0;
    } else if ((x[xOffset] == 0 && y[yOffset] > 0) || (x[xOffset] < 0 && y[yOffset] == 0)) {
        // (+0/-0): atan2(0, -0) is +π
        result[resultOffset] = FastMath.PI;
    } else if ((x[xOffset] == 0 && y[yOffset] < 0) || (x[xOffset] > 0 && y[yOffset] == 0)) {
        // (-0/+0): atan2(0, +0) is -π
        result[resultOffset] = -FastMath.PI;
    } else if (signX == signY) {
        // (+0,+0) or (-0,-0)
        add(tmp1, 0, x, xOffset, tmp1, 0);          // r + x
        divide(y, yOffset, tmp1, 0, result, resultOffset);       // y / (r + x)
        atan(result, resultOffset, result, resultOffset);         // atan(y / (r + x))
        for (int i = 0; i < result.length; ++i) {
            result[resultOffset + i] *= 2;
        }
    } else {
        // (-0,-0), (+0,+0)
        subtract(tmp1, 0, x, xOffset, tmp1, 0);     // r - x
        divide(y, yOffset, tmp1, 0, result, resultOffset);       // y / (r - x)
        atan(result, resultOffset, result, resultOffset);         // atan(y / (r - x))
        for (int i = 0; i < result.length; ++i) {
            result[resultOffset + i] *= 2;
        }
    }

    // fix value to take special cases (+0/+0, +0/-0, -0/+0, -0/-0, +/-infinity)
    if (Double.isInfinite(x[xOffset]) || Double.isInfinite(y[yOffset])) {
        result[resultOffset] = x[xOffset] > 0 ? FastMath.PI / 2 : -FastMath.PI / 2;
    }

}