public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    double realPart = rhs.getReal();
    double imaginaryPart = rhs.getImaginary();
    
    if (Double.isNaN(realPart) || Double.isNaN(imaginaryPart)) {
        return createComplex(Double.NaN, Double.NaN);
    }
    
    return createComplex(real + realPart, imaginary + imaginaryPart);
}