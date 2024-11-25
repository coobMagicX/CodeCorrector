public class AccurateMultiplication {
    
    public static double accurateMultiplication(double a, double b) {
        // Split the numbers into two parts to represent 53 bits precision for doubles
        long aHigh = (long) a;
        long aLow = Double.doubleToLongBits(a) - aHigh;
        
        long bHigh = (long) b;
        long bLow = Double.doubleToLongBits(b) - bHigh;

        // Perform multiplication on high parts
        long prodHigh = aHigh * bHigh;

        // Calculate the low part of the product using the bit representation
        // We add the high parts' products and subtract from the high part's product to get the carry
        long prodLow = (aHigh * bLow + aLow * bHigh) + (prodHigh - (aHigh * bHigh));

        // Since the result is in bits, convert it back to double
        long resultBits = (prodHigh << 52) | (prodLow & 0xFFFFFFFFFFFFF); // 52 bits for high part, rest for low part
        return Double.longBitsToDouble(resultBits);
    }

    public static void main(String[] args) {
        double a = 2.0;
        double b = 3.0;
        
        // Perform accurate multiplication
        double result = accurateMultiplication(a, b);

        System.out.println("The accurate product of " + a + " and " + b + " is: " + result);
    }
}