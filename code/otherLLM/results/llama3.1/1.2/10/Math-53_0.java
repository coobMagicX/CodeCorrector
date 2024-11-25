public Complex add(Complex rhs) 
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    // Check for NaN values
    if (Double.isNaN(real) || Double.isNaN(rhs.getReal()) 
        || Double.isNaN(imaginary) || Double.isNaN(rhs.getImaginary())) {

        // If either real or imaginary part is NaN, return NaN complex number
        return new Complex(Double.NaN, Double.NaN);
    }

    return createComplex(real + rhs.getReal(),
                           imaginary + rhs.getImaginary());
}