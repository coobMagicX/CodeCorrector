public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return NaN;
    }

    if (isInfinite) {
        // Changed this to return ZERO instead of Infinity, as infinity is not a valid value for a complex number in the context of reciprocals
        return ZERO;
    }

    double absReal = FastMath.abs(real);
    double absImaginary = FastMath.abs(imaginary);

    if (absReal < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}