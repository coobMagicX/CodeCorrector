public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    double realPart = this.real;
    double imaginaryPart = this.imaginary;
    double rhsReal = rhs.getReal();
    double rhsImaginary = rhs.getImaginary();

    // Adjust the real part if either is NaN
    if (Double.isNaN(realPart) || Double.isNaN(rhsReal)) {
        realPart = Double.NaN;
    } else {
        realPart += rhsReal;
    }

    // Adjust the imaginary part if either is NaN
    if (Double.isNaN(imaginaryPart) || Double.isNaN(rhsImaginary)) {
        imaginaryPart = Double.NaN;
    } else {
        imaginaryPart += rhsImaginary;
    }

    return createComplex(realPart, imaginaryPart);
}