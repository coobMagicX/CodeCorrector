public Complex divide(Complex other) {
    double realPart = this.real * other.real + this.imaginary * other.imaginary;
    double imaginaryPart = this.imaginary * other.real - this.real * other.imaginary;

    if (other.real == 0 && other.imaginary == 0) {
        throw new ArithmeticException("Cannot divide by zero");
    }

    return createComplex(realPart / (other.real * other.real + other.imaginary * other.imaginary),
            imaginaryPart / (other.real * other.real + other.imaginary * other.imaginary));
}