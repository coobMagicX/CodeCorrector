public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return NaN;
    }

    if (isInfinite) {
        return ZERO;
    }

    double scale;
    double q;

    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        q = real / imaginary;
        scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        q = imaginary / real;
        scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}