public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }

    Complex rhs = (Complex) other;
    return Double.compare(real, rhs.real) == 0 && Double.compare(imaginary, rhs.imaginary) == 0;
}
