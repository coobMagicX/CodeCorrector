public Complex add(Complex rhs)
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    if (Double.isNaN(this.real) || Double.isNaN(this.imaginary) ||
        Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
        // If any of the components are NaN, return a Complex with all NaN values
        return createComplex(Double.NaN, Double.NaN);
    }
    // Otherwise, proceed with normal addition
    return createComplex(this.real + rhs.getReal(),
        this.imaginary + rhs.getImaginary());
}