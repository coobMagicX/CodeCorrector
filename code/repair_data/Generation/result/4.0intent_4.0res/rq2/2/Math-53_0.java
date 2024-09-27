public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    if (Double.isNaN(real) || Double.isNaN(imaginary) || Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
        return createComplex(Double.NaN, Double.NaN);
    }
    return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
}