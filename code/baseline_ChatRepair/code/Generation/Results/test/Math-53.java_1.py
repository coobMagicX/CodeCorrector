public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    // Handling NaN values
    double newReal = real + rhs.getReal();
    if (Double.isNaN(newReal)) {
        newReal = Double.NaN;
    }
    
    double newImaginary = imaginary + rhs.getImaginary();
    if (Double.isNaN(newImaginary)) {
        newImaginary = Double.NaN;
    }
    
   