@Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    Complex rhs = (Complex) other;  // We can safely cast now.

    if (this.isNaN() && rhs.isNaN()) {
        return true;
    }
    
    // Compare considering floating-point precision
    return doubleEquals(this.real, rhs.getReal()) && doubleEquals(this.imaginary, rhs.getImaginary());
}

private boolean doubleEquals(double a, double b) {
    final double EPSILON = 1e-10; // Use a tolerance level for double comparison
    return Math.abs(a - b) < EPSILON;
}

@Override
public int hashCode() {
    // Using Objects.hash() or a similar function to factor in all relevant fields
    int result = 17;
    long realBits = Double.doubleToLongBits(real);
    int realHash = (int) (realBits ^ (realBits >>> 32));
    long imaginaryBits = Double.doubleToLongBits(imaginary);
    int imaginaryHash = (int) (imaginaryBits ^ (imaginaryBits >>> 32));
    result = 31 * result + realHash;
    result = 31 * result + imaginaryHash;
    return result;
}
