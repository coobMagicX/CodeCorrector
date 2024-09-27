public boolean equals(Object other) {
    boolean ret;

    if (this == other) {
        ret = true;
    } else if (other == null || getClass() != other.getClass()) {
        ret = false;
    } else {
        Complex rhs = (Complex) other;
        if (rhs.isNaN()) {
            ret = this.isNaN();
        } else {
            ret = Double.compare(real, rhs.getReal()) == 0 && Double.compare(imaginary, rhs.getImaginary()) == 0;
        }
    }

    return ret;
}