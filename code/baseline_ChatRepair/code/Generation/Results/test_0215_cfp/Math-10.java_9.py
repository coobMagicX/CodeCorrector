public void atan2(final double[] y, final int yOffset,
                  final double[] x, final int xOffset,
                  final double[] result, final int resultOffset) {
  
    double yVal = y[yOffset];
    double xVal = x[xOffset];

    // Handle infinity cases
    if (Double.isInfinite(xVal) || Double.isInfinite(yVal)) {
        result[resultOffset] = handleInfinite(yVal, xVal);
        return;
    }

    // Compute r = sqrt(x^2 + y^2)
    double r = Math.sqrt(xVal * xVal + yVal * yVal);

    // We should specifically handle the case if r plus/minus x goes to zero.
    double angle;

    if (xVal >= 0) {
        // r + x denoting that x >= 0, use the right quadrant
        double denom = r + xVal;
        angle = 2 * Math.atan(yVal / denom);
    } else {
        // Compute angle considering x < 0 to apply the correction by pi,
        // Here r - x and x < 0 so we subtract x taking into account its sign
        double denom = r - xVal;
        if (denom != 0) {
            angle = 2 * Math.atan(yVal / denom);
        } else {
            angle = yVal >= 0 ? FastMath.PI : -FastMath.PI;
        }
        angle = FastMath.PI - angle; // Adjust the angle further by PI
    }

    // Handle cases for negative x, shifting result from PI to -PI
    if (xVal == 0 && yVal != 0) {
        angle = yVal > 0 ? FastMath.PI * 0.5 : -FastMath.PI * 0.5;
    } else if (xVal == 0 && yVal == 0) {
        angle = 0; // Origin case
    }

    // Assign the result
    result[resultOffset] = angle;
}

private double handleInfinite(double yVal, double xVal) {
    if (Double.isInfinite(xVal) && Double.isInfinite(yVal)) {
        return FastMath.PI * 0.25 * Math.signum(yVal) * (2 - Math.signum(xVal));
    } else if (Double.isInfinite(xVal)) {
        return Math.signum(xVal) < 0 ? FastMath.PI : 0;
    } else { // y is infinite
        return FastMath.PI * 0.5 * Math.signum(yVal);
    }
}
