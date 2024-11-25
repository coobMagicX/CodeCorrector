public boolean equals(Object other) {
    if (this == other) { 
        return true;
    } else if (other == null || getClass() != other.getClass()) {
        return false;
    }

    Complex rhs = (Complex) other;

    // Check for NaN values which are not equal to anything.
    if (rhs.isNaN()) {
        return isNaN();
    }
    
    return (Double.compare(real, rhs.getReal()) == 0) && (Double.compare(imaginary, rhs.getImaginary()) == 0);
}