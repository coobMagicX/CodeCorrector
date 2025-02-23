public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    // Capture real and imaginary parts for propagation of NaN
    double newReal = real + rhs.getReal();
    double newImaginary = imaginary