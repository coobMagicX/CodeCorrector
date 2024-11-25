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
    
    public Complex multiply(Complex rhs) {
        double newReal = this.real * rhs.getReal() - this.imaginary * rhs.getImaginary();
        double newImaginary = this.real * rhs.getImaginary() + this.imaginary * rhs.getReal();
        return new Complex(newReal, newImaginary);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) { 
            return true;
        } else if (other == null) {
            return false;
        } else  {
            try {
                Complex rhs = (Complex)other;
                // Use the existing equals method to compare this object with the right-hand side
                return this.equals(rhs);
            } catch (ClassCastException ex) {
                // ignore exception
                return false;
            }
        }
    }

    public static void main(String[] args) {
        Complex c1 = new Complex(1, 0); // Real unit
        Complex c2 = new Complex(0, -1); // Imaginary unit (-i)
        Complex result = c1.multiply(c2);
        
        // Check if the multiplication of c1 and c2 equals to 1 + i
        System.out.println(result.equals(new Complex(1, 1)));
    }
}