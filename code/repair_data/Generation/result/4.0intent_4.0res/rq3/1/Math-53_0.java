public Complex add(Complex rhs) {
    if (rhs == null) {
        throw new IllegalArgumentException("Argument cannot be null");
    }
    return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
}