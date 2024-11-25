public class HighPrecisionMultiplication {
    
    private static final double SPLIT_FACTOR = 1e-13;

    public static double highPrecisionMultiply(double a, double b) {
        // Split the values into two parts to capture higher precision
        double aHigh = a - (a % SPLIT_FACTOR);
        double aLow = a - aHigh;
        double bHigh = b - (b % SPLIT_FACTOR);
        double bLow = b - bHigh;

        // Perform the multiplication
        double prodHigh = a * b;
        double prodLow = aLow * b + a * bLow + aHigh * bLow + bHigh * aLow;

        return prodHigh + prodLow;
    }

    public static void main(String[] args) {
        // Test with an example value where the multiplication might lose precision
        double result = highPrecisionMultiply(1.000000000000001, 2.000000000000002);
        System.out.println("The high-precision product is: " + result);
    }
}