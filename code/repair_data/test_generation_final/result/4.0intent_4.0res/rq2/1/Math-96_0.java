public boolean equals(Object other) {
    boolean ret;

    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (this.isNaN() || rhs.isNaN()) {
                ret = this.isNaN() && rhs.isNaN(); // Correct handling when either this or rhs is NaN
            } else {
                ret = (Double.doubleToRawLongBits(this.getReal()) == Double.doubleToRawLongBits(rhs.getReal())) &&
                      (Double.doubleToRawLongBits(this.getImaginary()) == Double.doubleToRawLongBits(rhs.getImaginary()));
            }
        } catch (ClassCastException ex) {
            ret = false;
        }
    }

    return ret;
}