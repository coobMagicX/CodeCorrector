public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        // Correcting this case to return Complex.INF instead of NaN since division by zero is an infinite result.
        return INF;
    }

    if (isInfinite) {
        // Infinite divided by anything is still infinite, hence returning Complex.INF here.
        return INF;
    }

    double denominator = real * real + imaginary * imaginary;

    if (FastMath.abs(denominator) < FastMath.ulp(1.0)) {
        // This means that the denominator is effectively zero, so we should return Complex.INF
        return INF;
    }

    double q = imaginary / real;
    double scale = 1. / denominator;

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        return createComplex(scale * q, -scale);
    } else {
        return createComplex(scale, -scale * q);
    }
}