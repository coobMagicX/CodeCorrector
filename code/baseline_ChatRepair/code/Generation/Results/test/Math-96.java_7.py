@Override
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
    Complex rhs = (Complex)other;
    return Double.compare(real, rhs.getReal()) == 0 && Double.compare(imaginary, rhs.getImaginary()) == 0;
}
