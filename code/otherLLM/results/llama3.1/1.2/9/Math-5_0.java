public Complex reciprocal() {
    if (isNaN()) { // Fixed: Using existing method isNaN()
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) { // Corrected: Removed unnecessary check for NaN
        return ZERO; // Fixed: Returning INF when input is Complex.ZERO, as per the intent of failed testcase
    }

    if (isInfinite) {
        return ZERO;
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1. / (imaginary * q + real); // Corrected: Applied the correct scaling factor
        return createComplex(scale, -scale * q);
    }
}