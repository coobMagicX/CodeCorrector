public static boolean equals(double x, double y) {
    if (Double.isNaN(x) || Double.isNaN(y)) {
        return Double.compare(x, y) == 0; // NaN is equal to NaN
    }
    return x == y;
}