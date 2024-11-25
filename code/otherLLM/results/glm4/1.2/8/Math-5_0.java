public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    // Check for a zero complex number correctly
    if ((real == 0.0 && imaginary == 0.0)) {
        return Complex.INF; // Return INF when both real and imaginary components are zero
    }

    if (isInfinite) {
        return ZERO;
    }

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        // Use an if-else structure to handle division by zero separately
        if (real == 0.0) {
            return Complex.INF; // Handle the case when real component is zero and imaginary is not
        }
        double q = imaginary / real;
        double scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}