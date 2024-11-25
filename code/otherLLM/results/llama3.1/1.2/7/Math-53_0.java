public Complex add(Complex rhs) 
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    // Check for NaN values before performing addition
    if (Double.isNaN(real) || Double.isNaN(imaginary) || Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
        throw new NullPointerException("Cannot add a complex number with NaN value.");
    }
    
    return createComplex(real + rhs.getReal(),
        imaginary + rhs.getImaginary());
}