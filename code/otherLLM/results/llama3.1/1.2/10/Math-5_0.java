public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return ZERO; // Return Complex.ZERO instead of NaN when real and imaginary are both zero.
    }

    if (isInfinite) {
        return ZERO;
    }

    double denominator = real * real + imaginary * imaginary;

    if (FastMath.abs(denominator) < 1e-12) { // Introduce a tolerance for division by very small numbers
        return Complex.INF; // Return Complex.INFINITY when the complex number is essentially zero.
    }

    if (real == imaginary && real != 0.0) {
        return createComplex(1. / (2 * real), -1. / (2 * real));
    } else {
        double scale = 1. / denominator;
        return createComplex(real * scale, imaginary * scale);
    }
}