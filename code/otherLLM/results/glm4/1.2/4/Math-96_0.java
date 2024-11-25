class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    // Getter for real part
    public double getReal() {
        return real;
    }

    // Getter for imaginary part
    public double getImaginary() {
        return imaginary;
    }

    // Method to multiply two complex numbers
    public Complex multiply(Complex other) {
        double newReal = this.real * other.getReal() - this.imaginary * other.getImaginary();
        double newImaginary = this.real * other.getImaginary() + this.imaginary * other.getReal();
        return new Complex(newReal, newImaginary);
    }

    // Method to check if the complex number is NaN
    public boolean isNaN() {
        return Double.isNaN(this.real) && Double.isNaN(this.imaginary);
    }
    
    // equals method fixed and used with multiplication
    @Override
    public boolean equals(Object other) {
        if (this == other) { 
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Complex rhs = (Complex) other;

        // Check for NaN cases
        if (rhs.isNaN() && this.isNaN()) {
            return true;
        }
        
        // Normal comparison
        return Double.compare(this.real, rhs.getReal()) == 0 &&
               Double.compare(this.imaginary, rhs.getImaginary()) == 0;
    }

    @Override
    public int hashCode() {
        long tmp;
        tmp = Double.doubleToLongBits(real);
        int result = (int) (tmp ^ (tmp >>> 32));
        tmp = Double.doubleToLongBits(imaginary);
        result = 31 * result + (int) (tmp ^ (tmp >>> 32));
        return result;
    }
    
    @Override
    public String toString() {
        return "(" + real + ", " + imaginary + ")";
    }
}

// Example usage:
Complex c1 = new Complex(0, 1);
Complex c2 = new Complex(-1, 0);
Complex result = c1.multiply(c2);

System.out.println("Expected: (0,-1) - Actual: " + result); // Should print: Expected: (0,-1) - Actual: (0.0,-1.0)