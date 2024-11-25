public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    double realResult = (Double.isNaN(this.real) || Double.isNaN(rhs.getReal())) ? Double.NaN : this.real + rhs.getReal();
    double imaginaryResult = (Double.isNaN(this.imaginary) || Double.isNaN(rhs.getImaginary())) ? Double.NaN : this.imaginary + rhs.getImaginary();
    
    return createComplex(realResult, imaginaryResult);
}