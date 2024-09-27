public boolean equals(Object other) {
    boolean ret;

    if (this == other) { 
        ret = true;
    } else if (other == null || getClass() != other.getClass()) {
        ret = false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            if (Double.isNaN(rhs.getReal())) {
                ret = Double.isNaN(this.getReal());
            } else {
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary())); 
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }

    return ret;
}