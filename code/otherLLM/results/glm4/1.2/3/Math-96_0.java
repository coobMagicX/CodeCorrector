public class Complex {
    private double real;
    private double imaginary;

    // Constructor for creating a complex number with given real and imaginary parts
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    // Method to check if the complex number is NaN (Not a Number)
    public boolean isNaN() {
        return Double.isNaN(real) || Double.isNaN(imaginary);
    }
    
    // Method to multiply two complex numbers
    public Complex multiply(Complex rhs) {
        if (isNaN() || rhs.isNaN()) {
            return Complex.NaN;
        }
        
        double newReal = this.real * rhs.real - this.imaginary * rhs.imaginary; // ac - bd
        double newImaginary = this.real * rhs.imaginary + this.imaginary * rhs.real; // ad + bc
        
        return new Complex(newReal, newImaginary); // Create a new complex number with the result
    }

    // Method to get the real part of the complex number
    public double getReal() {
        return real;
    }
    
    // Method to get the imaginary part of the complex number
    public double getImaginary() {
        return imaginary;
    }

    // Existing equals method (not directly relevant to multiplication repair)
    public boolean equals(Object other) {
        boolean ret;

        if (this == other) {
            ret = true;
        } else if (other == null) {
            ret = false;
        } else {
            try {
                Complex rhs = (Complex)other;
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
    
    // Static constants for NaN and Infinity, assuming they are defined in the class
    public static final Complex NaN = new Complex(Double.NaN, Double.NaN);
    public static final Complex INF = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    // toString method for printing complex numbers
    @Override
    public String toString() {
        return "(" + real + " + " + imaginary + "i)";
    }
}