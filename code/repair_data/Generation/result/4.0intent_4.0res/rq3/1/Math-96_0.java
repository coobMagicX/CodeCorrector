public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    
    Complex rhs = (Complex) other;
    if (this.isNaN() || rhs.isNaN()) {
        return this.isNaN() && rhs.isNaN();
    }
    
    return Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal()) &&
           Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary());
}