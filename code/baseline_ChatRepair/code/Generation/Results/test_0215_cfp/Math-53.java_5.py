public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);

    // Calculate new real part
    double newReal = real + rhs.getReal();
    if (Double.isNaN