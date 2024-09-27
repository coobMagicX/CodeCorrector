public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || (Math.abs(x - y) < 0.000001);
}