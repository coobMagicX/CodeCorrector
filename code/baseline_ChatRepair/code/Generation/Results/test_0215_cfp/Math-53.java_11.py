public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    double newReal = real + rhs.getReal();
    double newImaginary = imaginary + rhs.getImaginary();

    // Adjusting for NaN values
    if (Double.isNaN(real) || Double.isNaN(rhs.getReal())) {
        newReal = Double.NaN;
    }
    if (Double.isNaN(imaginary) || Double.isNaN(rhs.getImaginary())) {
        newImaginary