public static boolean equals(double x, double y) {
    return Double.compare(x, y) == 0 || (Double.isNaN(x) && Double.isNaN(y));
}