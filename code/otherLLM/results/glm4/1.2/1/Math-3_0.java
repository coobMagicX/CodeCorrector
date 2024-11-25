public class HighPrecisionMultiplication {
    private static final double SPLIT_FACTOR = 0.5;

    public static void main(String[] args) {
        // Example usage with two numbers:
        double num1 = 3.0;
        double num2 = 4.0;
        double result = highPrecisionMultiply(num1, num2);
        System.out.println("High precision multiplication: " + result);
    }

    public static double highPrecisionMultiply(double a, double b) {
        if (a == 0 || b == 0) return 0;

        // Split numbers into halves
        double a_half = a * SPLIT_FACTOR;
        double b_half = b * SPLIT_FACTOR;

        // High precision multiplication part (using the same method as the provided example)
        double prod_high = a * b; // This is the high part of the product
        double prod_low = (a_half - a) * (b_half - b) + (a_half - a) * b + a * (b_half - b); // This accounts for lower precision parts

        // Return high part plus low part scaled by 2
        return prod_high + prod_low;
    }
}