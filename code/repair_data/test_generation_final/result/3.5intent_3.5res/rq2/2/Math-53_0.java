public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    if (Double.isNaN(this.getReal()) || Double.isNaN(this.getImaginary()) ||
        Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
        return createComplex(Double.NaN, Double.NaN);
    }
    double newReal = this.getReal() + rhs.getReal();
    double newImaginary = this.getImaginary() + rhs.getImaginary();
    return createComplex(newReal, newImaginary);
}