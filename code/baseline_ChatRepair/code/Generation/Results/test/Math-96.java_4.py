public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null) {
        return false;
    }
    if (!(other instanceof Complex)) {
        return false;
    }

    Complex rhs = (Complex) other;
    if (this.isNaN() || rhs.isNaN()) {
        return this.isNaN() && rhs.isNaN();
    }

    return (Double.doubleToLongBits(real) == Double.doubleToLongBits(rhs.getReal())) &&
           (Double.doubleToLongBits(imaginary) == Double.doubleToLongBits(rhs.getImaginary()));
}
