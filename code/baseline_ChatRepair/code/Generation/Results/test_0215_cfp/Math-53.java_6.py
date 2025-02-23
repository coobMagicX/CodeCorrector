public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    double realPart = this.real + rhs.getReal();
    double imaginaryPart = this.imaginary + rhs.getImaginary();

    // Check if any of the components are NaN and adjust accordingly
    if (Double.isNaN(realPart)