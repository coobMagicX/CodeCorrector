public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    // Correcting the check for zero-complex number to return Infinity instead of NaN
    if (real == 0.0 && imaginary == 0.0) {
        return INFINITY; // Return Infinity for the reciprocal of a zero-complex number
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
        double scale = 1. / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}