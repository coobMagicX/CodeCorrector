public class Main {
    public static void main(String[] args) {
        System.out.println(multiply(1.0, 2.0, 3.0, 4.0));
    }

    /**
     * This function multiplies four numbers with minimal rounding errors.
     *
     * @param a1 First number
     * @param b1 Second number
     * @param a2 Third number
     * @param b2 Fourth number
     * @return Product of the four numbers
     */
    public static double multiply(double a1, double b1, double a2, double b2) {
        return multiply(a1, b1, multiply(a2, b2));
    }

    private static double multiply(double a, double b) {
        final int BITS = 26;
        double high = split(a, BITS);
        double low = split(b, BITS);

        double productHigh = split(a, BITS) * split(b, BITS);
        double productLow = split(split(a, BITS), BITS) * split(split(b, BITS), BITS)
                - (((productHigh - split(split(a, BITS), BITS) * split(split(b, BITS), BITS)) - split(split(a, BITS), BITS) * split(split(b, BITS), BITS))
                - split(split(a, BITS), BITS) * split(split(b, BITS), BITS));

        return add(add(high, productHigh), productLow);
    }

    private static double split(double num, int bits) {
        long sign = (num < 0) ? -1 : 1;
        num *= bits;
        long high = (long) Math.floor(num / 2);
        long low = ((long) num & (bits - 1)) * sign;

        return (double) high + low / bits;
    }

    private static double add(double a, double b) {
        int signA = (a < 0) ? -1 : 1;
        int signB = (b < 0) ? -1 : 1;
        double sum;

        if (signA == signB) {
            sum = a + b;
        } else {
            long highA = (long) Math.floor(Math.abs(a));
            long lowA = ((long) Math.abs(a) & (26 - 1)) * signA;
            long highB = (long) Math.floor(Math.abs(b));
            long lowB = ((long) Math.abs(b) & (26 - 1)) * signB;

            long sumHigh = add(highA, highB);
            long sumLow = add(lowA, lowB);

            if (sumHigh < 0 || sumHigh >= 2 * (1L << 26)) {
                sumHigh += sumLow / (1L << 26);
                sumLow %= (1L << 26);
            } else {
                sumLow += sumHigh;
            }

            sum = (double) sumHigh + (sumLow / (1L << 26));
        }

        return signA * signB == -1 ? -sum : sum;
    }
}