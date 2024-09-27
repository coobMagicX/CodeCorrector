public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || (x == y && Double.compare(x, y) == 0);
}