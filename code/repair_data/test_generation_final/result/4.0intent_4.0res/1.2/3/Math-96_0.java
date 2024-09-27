public boolean equals(Object other) {
    boolean ret;

    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) &&
                      (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary()));
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }

    return ret;
}

public Complex multiply(Complex rhs) {
    if (isNaN() || rhs.isNaN()) {
        return NaN;
    }
    if (Double.isInfinite(real) || Double.isInfinite(imaginary) ||
        Double.isInfinite(rhs.getReal()) || Double.isInfinite(rhs.getImaginary())) {
        // we don't use Complex.isInfinite() to avoid testing for NaN again
        return INF;
    }
    return createComplex(real * rhs.getReal() - imaginary * rhs.getImaginary(),
                         real * rhs.getImaginary() + imaginary * rhs.getReal());
}