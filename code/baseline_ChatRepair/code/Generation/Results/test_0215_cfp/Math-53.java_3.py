public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    double newReal = real + rhs.getReal();
    double newImaginary = imaginary + rhs.getImaginary();
    
    if (Double.isNaN(real) || Double.isNaN(rhs.getReal())) {
       