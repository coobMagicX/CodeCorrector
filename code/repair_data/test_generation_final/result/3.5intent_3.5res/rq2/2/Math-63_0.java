public static boolean equals(double x, double y) {
    if (Double.isNaN(x) && Double.isNaN(y)) {
        return true;
    }
    double[] values = {x, y};
    try {
        double[] normalizedValues = normalizeArray(values, 1);
        return normalizedValues[0] == normalizedValues[1];
    } catch (ArithmeticException | IllegalArgumentException e) {
        return false;
    }
}