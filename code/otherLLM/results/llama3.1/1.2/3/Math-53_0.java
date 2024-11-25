public Complex add(Complex rhs) 
throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    
    double realSum = addOrSubtract(real, rhs.getReal(), "add");
    double imaginarySum = addOrSubtract(imaginary, rhs.getImaginary(), "imaginary");

    return createComplex(realSum, imaginarySum);
}

private double addOrSubtract(double value1, double value2, String valueName) {
    if (Double.isNaN(value1) && Double.isNaN(value2)) {
        return Double.NaN;
    }
    
    if (Double.isNaN(value1)) {
        return value2;
    }
    if (Double.isNaN(value2)) {
        return value1;
    }

    return value1 + value2;
}