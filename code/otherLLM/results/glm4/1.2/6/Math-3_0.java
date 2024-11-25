public class HighPrecisionMultiplication {

    public static double accurateMultiply(double a, double b) {
        long la = Double.doubleToLongBits(a);
        long lb = Double.doubleToLongBits(b);

        // Split the high and low parts of the binary representation
        long highA = (la & 0x7FF0000000000000L) >> 52;
        long lowA = la & 0xFFFFFFFFFFFFFull;
        long highB = (lb & 0x7FF0000000000000L) >> 52;
        long lowB = lb & 0xFFFFFFFFFFFFFull;

        // Calculate the high and low parts of the product
        long highProduct = highA * highB;
        long lowProduct = (highA * lowB + lowB * highB);

        // Combine high and low parts into a single long value
        long combinedHighLow = ((long) highProduct << 52) | lowProduct;

        // Convert the combined long value back to a double
        return Double.longBitsToDouble(combinedHighLow);
    }

    public static void main(String[] args) {
        double a = 1.23456789;
        double b = 2.3456789;

        double result = accurateMultiply(a, b);
        System.out.println("Accurate product of " + a + " and " + b + " is: " + result);
    }
}