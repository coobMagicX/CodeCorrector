public static boolean equals(double x, double y) {
    if ((Double.isNaN(x) && Double.isNaN(y)) || (x == y)) {
        return true;
    }
    if (Double.isNaN(x) || Double.isNaN(y)) {
        return false;
    }
    return equals(new double[] {x}, new double[] {y});
}