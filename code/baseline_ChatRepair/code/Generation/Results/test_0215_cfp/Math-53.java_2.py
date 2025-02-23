public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    // Correctly handle NaN propagation in real and imaginary parts
    double newReal = real + rhs.getReal();
    double newImaginary = imaginary + rhs.getImaginary();

    if (Double.isNaN(real) || Double.isNaN(rhs.getReal())) {
        newReal = Double.NaN;
    }
    if (Double.isNaN(imaginary) || Double.isNaN(rhs.getImaginary())) {
       