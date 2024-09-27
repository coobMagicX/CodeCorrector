public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    if (Double.isNaN(this.getReal()) || Double.isNaN(this.getImaginary()) || Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
        return createComplex(Double.NaN, Double.NaN);
    }
    return createComplex(this.getReal() + rhs.getReal(), this.getImaginary() + rhs.getImaginary());
}