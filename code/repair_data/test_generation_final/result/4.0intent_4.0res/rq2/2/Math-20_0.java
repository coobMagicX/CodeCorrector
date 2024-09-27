public double[] repairAndDecode(final double[] x) {
    double maxValue = max(x);
    if (maxValue != 0) {  // Ensure no division by zero
        for (int i = 0; i < x.length; i++) {
            x[i] /= maxValue;
        }
    }
    return x;
}