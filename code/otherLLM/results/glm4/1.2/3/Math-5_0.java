import static java.lang.Math.abs;

public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        // Return Complex.INF instead of NaN for a zero complex number
        return Complex.INF;
    }

    if (isInfinite) {
        return ZERO;
    }

    if (abs(real) < abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1.0 / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1.0 / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}