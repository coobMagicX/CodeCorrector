public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    double newReal = Double.isNaN(this.real) || Double.isNaN(rhs.getReal()) ? Double.NaN : this.real + rhs.getReal();
    double newImaginary = Double.isNaN(this.imaginary) || Double.isNaN(rhs.getImaginary()) ? Double.NaN : this.imaginary + rhs.getImaginary();

    return createComplex(newReal, newImaginary);
}
