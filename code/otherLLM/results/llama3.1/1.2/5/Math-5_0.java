public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return Complex.INFINITY; // Correctly return infinity for zero complex number
    }

    if (isInfinite) {
        return ZERO;
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary); // Avoid division by zero
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        if (FastMath.abs(real) == FastMath.abs(imaginary)) { // Check for division by zero
            scale = 1.0 / (imaginary * q + real); // Avoid division by zero
        } else {
            scale = 1.0 / (imaginary * q + real);
        }
        return createComplex(scale, -scale * q);
    }
}