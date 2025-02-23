public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    
    if (!(other instanceof Complex)) {
        return false;
    }
    
    Complex rhs = (Complex) other;
    if (rhs.isNaN() && this.isNaN()) {
        return true;
    }

    return Double.compare(real, rhs.getReal()) == 0 && Double.compare(imaginary, rhs.getImaginary()) == 0;
}
