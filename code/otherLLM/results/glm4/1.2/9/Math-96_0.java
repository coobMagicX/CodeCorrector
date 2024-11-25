class Complex {
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
        double newReal = this.real * other.getReal() - this.imaginary * other.getImaginary();
        double newImaginary = this.real * other.getImaginary() + this.imaginary * other.getReal();
        return new Complex(newReal, newImaginary);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) { 
            return true;
        } else if (other == null || !(other instanceof Complex)) {
            return false;
        } else {
            Complex rhs = (Complex) other;
            // Check for NaN values and equality of real and imaginary parts
            if (rhs.isNaN() && this.isNaN()) {
                return true; // Both are NaN, so they are equal
            }
            
            boolean isRealEqual = Double.compare(this.real, rhs.getReal()) == 0;
            boolean isImaginaryEqual = Double.compare(this.imaginary, rhs.getImaginary()) == 0;

            return isRealEqual && isImaginaryEqual;
        }
    }

    @Override
    public int hashCode() {
        long realBits = Double.doubleToLongBits(real);
        long imagBits = Double.doubleToLongBits(imaginary);
        return (int)(realBits ^ (imagBits >>> 32));
    }
    
    // Additional method to test the multiplication logic
    public static void main(String[] args) {
        Complex c1 = new Complex(2, -1); // This should multiply with another complex number to get i
        Complex c2 = new Complex(0, 1);
        
        Complex result = c1.multiply(c2);
        System.out.println("Result: " + result.getReal() + " + " + result.getImaginary() + "i");
    }
}