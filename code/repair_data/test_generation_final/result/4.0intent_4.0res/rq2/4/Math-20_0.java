public double[] repairAndDecode(final double[] x) {
    for (int i = 0; i < x.length; i++) {
        if (x[i] > 0.5) {
            x[i] = 0.5;
        } else if (x[i] < -0.5) {
            x[i] = -0.5;
        }
    }
    return decode(x);
}