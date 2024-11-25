public Complex add(Complex rhs)
throws NullArgumentException {
MathUtils.checkNotNull(rhs);

// Check if either the lhs or rhs Complex object has NaN values and handle it accordingly.
if (Double.isNaN(real) || Double.isNaN(imaginary) || Double.isNaN(rhs.getReal()) || Double.isNaN(rhs.getImaginary())) {
    // If either real or imaginary part is NaN, return a new Complex object with NaN values
    return createComplex(Double.NaN, Double.NaN);
}

return createComplex(real + rhs.getReal(),
        imaginary + rhs.getImaginary());
}