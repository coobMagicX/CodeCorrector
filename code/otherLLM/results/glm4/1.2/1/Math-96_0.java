public boolean equals(Object other) {
    boolean ret;

    if (this == other) { 
        ret = true;
    } else if (other == null) {
        ret = false;
    } else  {
        try {
            Complex rhs = (Complex)other;
            if (rhs.isNaN() || this.isNaN()) {
                ret = false; // Both NaN or one is NaN, they are not equal
            } else {
                double realDiff = Math.abs(real - rhs.getReal());
                double imagDiff = Math.abs(imaginary * rhs.getImaginary()); // Use multiplication for imaginary parts

                // Check if the difference between the real and imaginary parts is within an epsilon value (for floating point comparison)
                ret = (realDiff <= Double.MIN_NORMAL) && (imagDiff <= Double.MIN_NORMAL);
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }

    return ret;
}