public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    double newReal = real;
    double newImaginary = imaginary;

    if (!Double.isNaN(rhs.getReal())) {
        newReal += rhs.getReal();
    } else {
        newReal = Double.NaN;
    }

    if (!Double.isNaN(rhs.getImaginary())) {
        newImaginary += rhs.getImaginary();
    } else {
        newImaginary = Double.NaN;
    }

    return createComplex(newReal, newImaginary);
}