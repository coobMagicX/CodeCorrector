public Complex add(Complex rhs)
throws NullArgumentException {
MathUtils.checkNotNull(rhs);
double real = this.real + rhs.getReal();
double imaginary = this.imaginary + rhs.getImaginary();

// Check for NaN values in the input parameters and propagate them correctly
if (Double.isNaN(real) || Double.isNaN(imaginary)) {
    return createComplex(Double.NaN, Double.NaN);
}
return createComplex(real, imaginary);
}