public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    
    Complex rhs = (Complex) other;
    if (isNaN() || rhs.isNaN()) {
        return isNaN() && rhs.isNaN();
    }
    
    return (compare(real, rhs.real) == 0 && compare(imaginary, rhs.imaginary) == 0);
}

/**
 * Helper method to compare two doubles for equality taking care of precision issues and special numbers.
 */
private int compare(double d1, double d2) {
    if (Double.isNaN(d1) || Double.isNaN(d2)) {
        return Double.isNaN(d1) && Double.isNaN(d2) ? 0 : 1;
    }
    if (Double.isInfinite(d1) || Double.isInfinite(d2)) {
        return d1 == d2 ? 0 : (d1 > d2 ? 1 : -1);
    }
    double diff = Math.abs(d1 - d2);
    if (diff < 1e-10) {
        return 0;
    } else {
        return d1 > d2 ? 1 : -1;
    }
}
