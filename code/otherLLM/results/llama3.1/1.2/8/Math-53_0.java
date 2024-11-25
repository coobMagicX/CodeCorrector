public Complex add(Complex rhs)
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    // Check if either of the inputs is NaN, return NaN for both real and imaginary parts
    if (rhs.isNaN || isNaN) {
        return createComplex(Double.NaN, Double.NaN);
    }
    
    return createComplex(real + rhs.getReal(),
        imaginary + rhs.getImaginary());
}