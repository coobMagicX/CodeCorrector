public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    double realPart = this.real + rhs.getReal();
    double imaginaryPart = this.imaginary + rhs.getImaginary();

    // When any of the real or imaginary parts of the input numbers is NaN,
    // then the corresponding result part must also be NaN.
    if (Double.isNaN(this.real) || Double.isNaN(rhs.getReal())) {
        realPart = Double.NaN;
    }
    if (Double.isNaN