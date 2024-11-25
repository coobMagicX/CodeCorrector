public Complex add(Complex rhs) 
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double real = this.real;
    if (Double.isNaN(this.real)) { // check for NaN values in 'this'
        return createComplex(Double.NaN, imaginary + rhs.getImaginary()); // propagate NaN value to result
    }
    double imaginary = this.imaginary;
    if (Double.isNaN(imaginary)) { // check for NaN values in 'imaginary'
        return createComplex(real + rhs.getReal(), Double.NaN); // propagate NaN value to result
    }
    return createComplex(real + rhs.getReal(),
            imaginary + rhs.getImaginary());
}