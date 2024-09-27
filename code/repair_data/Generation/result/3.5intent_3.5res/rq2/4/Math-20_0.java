public double[] repairAndDecode(final double[] x) {
    double[] repairedX = repair(x);
    return decode(repairedX);
}

public double[] repair(final double[] x) {
    double[] repairedX = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        double repairedValue = x[i];
        if (repairedValue < 0.0) {
            repairedValue = 0.0;
        } else if (repairedValue > 1.0) {
            repairedValue = 1.0;
        }
        repairedX[i] = repairedValue;
    }
    return repairedX;
}