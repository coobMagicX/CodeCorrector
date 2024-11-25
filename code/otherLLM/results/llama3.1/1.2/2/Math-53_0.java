public Complex add(Complex rhs)
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double realResult = isNan(real) || isNan(rhs.getReal()) ? Double.NaN : real + rhs.getReal();
    double imaginaryResult = isNan(imaginary) || isNan(rhs.getImaginary()) ? Double.NaN : imaginary + rhs.getImaginary();
    return createComplex(realResult, imaginaryResult);
}