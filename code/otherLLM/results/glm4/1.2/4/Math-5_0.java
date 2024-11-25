public Complex reciprocal() {
    if (isNaN) {
        return NaN; // This is fine for handling NaN cases.
    }

    if (real == 0.0 && imaginary == 0.0) {
        return Complex.INF; // Return Complex.INF when both real and imaginary are zero.
    }

    if (isInfinite) {
        return Complex.INF; // Return Complex.INF instead of ZERO for infinite values.
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