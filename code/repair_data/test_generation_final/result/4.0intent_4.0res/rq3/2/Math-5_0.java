public Complex reciprocal() {
    if (isNaN) {
        return NaN; // Assuming NaN is a static constant of type Complex defined elsewhere
    }

    if (real == 0.0 && imaginary == 0.0) {
        return NaN; // Assuming NaN is a static constant of type Complex defined elsewhere
    }

    if (isInfinite) {
        return ZERO; // Assuming ZERO is a static constant of type Complex defined elsewhere
    }

    if (Math.abs(real) < Math.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}