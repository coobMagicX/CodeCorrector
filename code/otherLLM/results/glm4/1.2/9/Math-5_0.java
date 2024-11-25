public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    // Check for zero complex number and return infinity (indeterminate form)
    if (real == 0.0 && imaginary == 0.0) {
        return INF; // Changed from NaN to INF
    }

    if (isInfinite) {
        return ZERO; // Return zero complex number since reciprocal of infinite is not defined
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}