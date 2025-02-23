private void guessAOmega() {
    
    double sx2 = 0;
    double sy2 = 0;
    double sxy = 0;
    double sxz = 0;
    double syz = 0;

    double currentX = observations[0].getX();
    double currentY = observations[0].getY();
    double f2Integral = 0;
    double fPrime2Integral = 0;
    final double startX = currentX;
    for (int i = 1; i < observations.length; ++i) {
        final double previousX = currentX;
        final double previousY = currentY;
        currentX = observations[i].getX();
        currentY = observations[i].getY();

        final double dx = currentX - previousX;
        final double dy = currentY - previousY;

        if (dx == 0) {
            throw new ZeroException("Zero delta x encountered, indicating non-monotonic X values or repeated observations.");
        }

        final double f2StepIntegral = dx * (previousY * previousY + previousY * currentY + currentY * currentY) / 3;
        final double fPrime2StepIntegral = dy * dy / dx;

        final double x = currentX - startX;
        f2Integral += f2StepIntegral;
        fPrime2Integral += fPrime2StepIntegral;

        sx2 += x * x;
        sy2 += f2Integral * f2Integral;
        sxy += x * f2Integral;
        sxz += x * fPrime2Integral;
        syz += f2Integral * fPrime2Integral;
    }

    double c1 = sy2 * sxz - sxy * syz;
    double c2 = sxy * sxz - sx2 * syz;
    double c3 = sx2 * sy2 - sxy * sxy;

    if (c1 == 0 || c2 == 0 || c3 == 0) {
        throw new MathIllegalStateException("Computed coefficients resulted in zero, likely due to insufficient or inappropriate data points.");
    }

    if ((c1 / c2 < 0) || (c2 / c3 < 0)) {
        final int last = observations.length - 1;
        final double xRange = observations[last].getX() - observations[0].getX();
        if (xRange == 0) {
            throw new ZeroException("No range in x values detected.");
        }
        omega = 2 * Math.PI / xRange;

        double yMin = Double.POSITIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < observations.length; ++i) {
            final double y = observations[i].getY();
            if (y < yMin) {
                yMin = y;
            }
            if (y > yMax) {
                yMax = y;
            }
        }
        a = 0.5 * (yMax - yMin);
    } else {
        a = Math.sqrt(Math.abs(c1 / c2));
        omega = Math.sqrt(Math.abs(c2 / c3));
    }
}
