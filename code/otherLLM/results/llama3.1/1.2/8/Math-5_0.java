public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (isInfinite) {
        return ZERO;
    }

    if (real == 0.0 && imaginary != 0.0) { // added check for non-zero imaginary part
        double q = imaginary / real; // changed the order of division
        double scale = 1. / (imaginary * q + real);
        return createComplex(-scale, scale); // corrected to match the expected sign
    } else if (real != 0.0 && imaginary == 0.0) {
        return ONE;
    } else { // added separate check for non-zero real and imaginary parts
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    }
}