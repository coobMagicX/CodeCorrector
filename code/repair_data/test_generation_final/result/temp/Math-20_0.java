public double[] repairAndDecode(final double[] x) {
    double[] repairedX = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        if (x[i] > 5.0) {
            repairedX[i] = 5.0;
        } else if (x[i] < -5.0) {
            repairedX[i] = -5.0;
        } else {
            repairedX[i] = x[i];
        }
    }
    return decode(repairedX);
}