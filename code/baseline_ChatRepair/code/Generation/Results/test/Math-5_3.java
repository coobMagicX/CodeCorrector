public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }

    if (real == 0.0 && imaginary == 0.0) {
        return INF; 
    }

    if (isInfinite) {
        return Complex.ZERO; 
    }

    double scale;
    if (Math.abs(real) <= Math.abs(imaginary)) {
        double q = real / imaginary;
        scale = 1.0 / (real * q + imaginary);
        return new Complex(scale * q, -scale); 
    } else {
        double q = imaginary / real;
        scale = 1.0 / (imaginary * q + real);
        return new Complex(scale, -scale * q); 
    }
}
