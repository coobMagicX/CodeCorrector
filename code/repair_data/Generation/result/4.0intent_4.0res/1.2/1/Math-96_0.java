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

    public Complex multiply(Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return new Complex(Double.NaN, Double.NaN);
        }
        if (Double.isInfinite(real) || Double.isInfinite(imaginary) ||
            Double.isInfinite(rhs.real) || Double.isInfinite(rhs.imaginary)) {
            return new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        return new Complex(real * rhs.real - imaginary * rhs.imaginary,
                real * rhs.imaginary + imaginary * rhs.real);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null) {
            return false;
        } else {
            try {
                Complex rhs = (Complex)other;
                if (rhs.isNaN()) {
                    return this.isNaN();
                } else {
                    return (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) &&
                           (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary()));
                }
            } catch (ClassCastException ex) {
                return false;
            }
        }
    }
}