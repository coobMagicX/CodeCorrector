public boolean equals(Object other) {
    boolean ret;

    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else if (!(other instanceof Complex)) {
        ret = false;
    } else {
        Complex rhs = (Complex) other;
        ret = (Math.abs(real - rhs.getReal()) < 1e-10) && (Math.abs(imaginary - rhs.getImaginary()) < 1e-10);
    }

    return ret;
}