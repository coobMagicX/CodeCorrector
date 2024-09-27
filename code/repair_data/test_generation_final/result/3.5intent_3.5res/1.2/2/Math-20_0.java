public double[] repairAndDecode(final double[] x) {
    double[] repairedX = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        repairedX[i] = Math.min(Math.max(x[i], 0.0), 1.0); // Ensure values are within [0, 1]
    }
    return decode(repairedX);
}