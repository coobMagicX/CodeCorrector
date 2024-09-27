public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    return createComplex(this.real + rhs.getReal(), this.imaginary + rhs.getImaginary());
}