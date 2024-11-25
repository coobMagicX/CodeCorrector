public boolean equals(Object other) {
    if (this == other) { 
        return true;
    } else if (other == null) {
        return false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            // Check for NaN or Infinite values before comparing
            if (rhs.isNaN() || isNaN()) {
                return this.isNaN() && rhs.isNaN();
            }
            
            // Calculate the negative complex conjugate using multiplication and compare
            Complex negativeConjugate = multiply(rhs.negative());
            return (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(negativeConjugate.getReal())) &&
                   (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(-negativeConjugate.getImaginary()));
        } catch (ClassCastException ex) {
            // ignore exception
            return false;
        }
    }
}

// Assuming the Complex class has a method negative() that returns the negative complex conjugate of the number:
public Complex negative() {
    return createComplex(-real, -imaginary);
}