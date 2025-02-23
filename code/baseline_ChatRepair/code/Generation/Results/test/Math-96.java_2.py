@Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    Complex rhs = (Complex) other;  

    if (this.isNaN() && rhs.isNaN()) {
        return true;
    }

    // Using a precision-specific comparison for double values.
    final double EPSILON = 1e-10; // Define a tolerance level for comparison
    return Math.abs(this.real - rhs.getReal()) < EPSILON && Math.abs(this.imaginary - rhs.getImaginary()) < EPSILON;
}
