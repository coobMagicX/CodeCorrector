public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    Complex rhs = (Complex) other;  // Safe to cast now since we checked the classes are the same

    if (this.isNaN() && rhs.isNaN()) {
        return true;
    }

    return doubleEquals(this.real, rhs.getReal()) && doubleEquals(this.imaginary, rhs.getImaginary());
}

private boolean doubleEquals(double a, double b) {
    final double EPSILON = 1e-10; // or an appropriate small value depending on precision requirements
    return Math.abs(a - b) < EPSILON;
}
