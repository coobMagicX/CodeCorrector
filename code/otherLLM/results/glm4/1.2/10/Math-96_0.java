public boolean equals(Object other) {
    if (this == other) { 
        return true;
    } else if (other == null || !(other instanceof Complex)) {
        return false;
    }

    Complex rhs = (Complex) other;

    // Check for NaN values first to ensure the comparison is correct.
    if (rhs.isNaN()) {
        return this.isNaN();
    }

    // Compare real and imaginary parts using a tolerance for floating-point arithmetic
    final double EPSILON = 1e-9;
    boolean equalsReal = Math.abs(this.getReal() - rhs.getReal()) < EPSILON;
    boolean equalsImaginary = Math.abs(this.getImaginary() - rhs.getImaginary()) < EPSILON;

    return equalsReal && equalsImaginary;
}