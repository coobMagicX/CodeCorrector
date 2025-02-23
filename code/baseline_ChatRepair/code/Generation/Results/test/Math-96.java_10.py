public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (other == null || getClass() != other.getClass()) {
        return false;
    }
    
    Complex rhs = (Complex) other;
    if (Double.isNaN(real) || Double.isNaN(imaginary) || Double.isNaN(rhs.real) || Double.isNaN(rhs.imaginary)) {
        return Double.isNaN(real) && Double.isNaN(imaginary) && Double.isNaN(rhs.real) && Double.isNaN(rhs.imaginary);
    }

    return Double.compare(real, rhs.real) == 0 && Double.compare(imaginary, rhs.imaginary) == 0;
}
