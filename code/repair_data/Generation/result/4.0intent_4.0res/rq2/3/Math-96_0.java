public class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imaginary);
    }

    public Complex multiply(Complex other) {
        double newReal = real * other.getReal() - imaginary * other.getImaginary();
        double newImaginary = real * other.getImaginary() + imaginary * other.getReal();
        return new Complex(newReal, newImaginary);
    }

    public boolean equals(Object other) {
        boolean ret;

        if (this == other) {
            ret = true;
        } else if (other == null) {
            ret = false;
        } else {
            try {
                Complex rhs = (Complex) other;
                if (rhs.isNaN()) {
                    ret = this.isNaN();
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
}