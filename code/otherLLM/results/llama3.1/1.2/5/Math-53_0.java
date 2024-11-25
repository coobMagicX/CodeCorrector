public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double real = this.real + rhs.getReal();
    double imaginary = this.imaginary + rhs.getImaginary();
    
    if (Double.isNaN(real)) return createComplex(Double.NaN, imaginary);
    if (Double.isNaN(imaginary)) return createComplex(real, Double.NaN);

    return createComplex(real, imaginary);
}