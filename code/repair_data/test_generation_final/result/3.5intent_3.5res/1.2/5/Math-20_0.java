public double[] repairAndDecode(final double[] x) {
    double[] repaired = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        if (x[i] > 5) {
            repaired[i] = 5;
        } else if (x[i] < -5) {
            repaired[i] = -5;
        } else {
            repaired[i] = x[i];
        }
    }
    return decode(repaired);
}