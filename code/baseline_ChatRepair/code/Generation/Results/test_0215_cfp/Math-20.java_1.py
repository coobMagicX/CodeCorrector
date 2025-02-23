public double[] repairAndDecode(final double[] x) {
    double[] decoded = decode(x);
    double upperBound = 0.5; // Define the upper bound constraint as per test assertion error

    for (int i