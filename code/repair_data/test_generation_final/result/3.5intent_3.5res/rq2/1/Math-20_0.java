public double[] repairAndDecode(final double[] x) {
    double[] repairedX = repair(x);
    return decode(repairedX);
}

private double[] repair(final double[] x) {
    double max = max(x);
    for (int i = 0; i < x.length; i++) {
        if (x[i] > max) {
            x[i] = max;
        }
    }
    return x;
}

private double[] decode(final double[] x) {
    // implementation of decode method
}