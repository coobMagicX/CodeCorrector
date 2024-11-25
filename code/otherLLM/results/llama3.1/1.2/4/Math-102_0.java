public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
if ((expected.length < 2) || (expected.length != observed.length)) {
throw new IllegalArgumentException(
        "observed, expected array lengths incorrect");
}
if (!isNonNegative(expected) || !isPositive(observed)) {
throw new IllegalArgumentException(
    "observed counts must be non-negative and expected counts must be positive");
}
double sumSq = 0.0d;
for (int i = 0; i < observed.length; i++) {
        double dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
}
return sumSq;
}