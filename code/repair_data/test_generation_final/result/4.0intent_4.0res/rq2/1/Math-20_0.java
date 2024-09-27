public double[] repairAndDecode(final double[] x) {
    double maxValue = max(x);
    for (int i = 0; i < x.length; i++) {
        if (x[i] > maxValue) {
            x[i] = maxValue;
        }
    }
    return x;
}