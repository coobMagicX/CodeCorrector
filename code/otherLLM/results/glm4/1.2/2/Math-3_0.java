public class VectorOperations {
    // A method to calculate the dot product of two vectors using Kahan summation
    public static double dotProduct(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length.");
        }

        double sum = 0.0;
        double c = 0.0; // A running compensation for lost low-order bits.

        for (int i = 0; i < vector1.length; i++) {
            double y = vector1[i] - c;    // So far, so good: c is zero.
            double t = sum + y;
            c = (t - sum) - y;           // Now, c is added to the previous iteration's sum.
            sum = t;                     // Algebraically, c should always be zero. Beware eagerly optimizing compilers!
        }

        return sum;
    }

    public static void main(String[] args) {
        double[] vector1 = {1.0, 2.0, 3.0};
        double[] vector2 = {4.0, 5.0, 6.0};

        double result = dotProduct(vector1, vector2);
        System.out.println("Dot product: " + result);
    }
}