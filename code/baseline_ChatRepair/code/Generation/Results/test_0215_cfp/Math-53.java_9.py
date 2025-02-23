public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double newReal = real + rhs.getReal();
    double newImaginary = imaginary + rhs.getImaginary();

    // Check for NaN in the components and return a Complex with NaN as required
    if (Double.isNaN