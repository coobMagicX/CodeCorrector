public Complex multiply(Complex rhs) {
    if (isNaN() || rhs.isNaN()) {
        return NaN;
    }
    if (Double.isInfinite(real) || Double.isInfinite(imaginary) ||
        Double.isInfinite(rhs.real) || Double.isInfinite(rhs.imaginary)) {
        // we don't use Complex.isInfinite() to avoid testing for NaN again
        return INF;
    }
    return createComplex(real * rhs.real - imaginary * rhs.imaginary,
            real * rhs.imaginary + imaginary * rhs.real);
}