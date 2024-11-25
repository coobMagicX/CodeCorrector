public class AccurateCalculator {
    public static double accurateAddition(double a1High, double a2Low, double b3Prime, double s4Low) {
        // Perform accurate addition by utilizing the sHigh, sPrime, and sLow variables
        return a1High + b3Prime + s4Low;
    }

    public static double accurateMultiplication(int[] numbers) {
        int numElements = numbers.length / 2; // Each pair of numbers represents two elements (a,b)
        double result = 0;

        for (int i = 0; i < numElements; i++) {
            double aHigh = numbers[i * 2] - (numbers[i * 2] - numbers[i * 2]);
            double aLow = numbers[i * 2] - aHigh;
            double bHigh = numbers[(i + 1) * 2] - (numbers[(i + 1) * 2] - numbers[(i + 1) * 2]);
            double bLow = numbers[(i + 1) * 2] - bHigh;

            // Perform accurate multiplication of a and b
            double prodHigh = aHigh * bHigh;
            double prodLow = aLow * bLow - ((prodHigh - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow;

            result += prodHigh + prodLow;
        }

        return result;
    }
}