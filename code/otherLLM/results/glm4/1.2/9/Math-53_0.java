public Complex add(Complex rhs) throws NullArgumentException {
    if (rhs == null) {
        throw new NullArgumentException("Right-hand side cannot be null.");
    }

    double realSum = Double.NaN;
    double imaginarySum = Double.NaN;

    // Check if either operand is NaN and set the sums to NaN
    if (Double.isNaN(real) || Double.isNaN(rhs.getReal())) {
        realSum = Double.NaN;
    } else {
        realSum = real + rhs.getReal();
    }

    if (Double.isNaN(imaginary) || Double.isNaN(rhs.getImaginary())) {
        imaginarySum = Double.NaN;
    } else {
        imaginarySum = imaginary + rhs.getImaginary();
    }

    return createComplex(realSum, imaginarySum);
}

// Method for creating a new Complex object
private Complex createComplex(double realPart, double imagPart) {
    // Implementation of Complex creation
}