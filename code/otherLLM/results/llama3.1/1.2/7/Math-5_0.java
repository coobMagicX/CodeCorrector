public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return ZERO; // Corrected to return ZERO instead of NaN for Complex.ZERO
    }

    if (isInfinite) {
        return ZERO;
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else if (FastMath.abs(imaginary) == FastMath.abs(real)) { // Added a condition to check for equality
        return ZERO; // Return ZERO when both real and imaginary components are equal in magnitude
    } else {
        double q = imaginary / real;
        double scale = 1. / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}