public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    // Calculate the real and imaginary parts by adding
    double realPart = this.real + rhs.getReal();
    double imaginaryPart = this.imaginary + rhs.getImaginary();

    // Handle NaN. If any part becomes NaN due to an operation, the result should explicitly be NaN
    if (Double.isNaN(this.real) || Double.isNaN(rhs.getReal())) {
        realPart = Double.NaN;
    }
    if (Double.is