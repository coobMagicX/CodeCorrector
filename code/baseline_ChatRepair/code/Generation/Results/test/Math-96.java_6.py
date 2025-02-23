@Override
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof Complex)) {
        return false;
    }
    Complex rhs = (Complex)other;
    return (Double.compare(real, rhs.getReal()) == 0) && (Double.compare(imaginary, rhs.getImaginary()) == 0);
}

@Override
public int hashCode() {
    int result = 17;
    long realBits = Double.doubleToLongBits(real);
    long imagBits = Double.doubleToLongBits(imaginary);
    result = 31 * result + (int) (realBits ^ (realBits >>> 32));
    result = 31 * result + (int) (imagBits ^ (imagBits >>> 32));
    return result;
}
