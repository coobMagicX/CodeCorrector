public Complex add(Complex rhs)
    throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    // Check if either operand is NaN
    if (this.isNaN || rhs.isNaN) {
        return Complex.NAN;
    }

    return createComplex(real + rhs.getReal(),
        imaginary + rhs.getImaginary());
}