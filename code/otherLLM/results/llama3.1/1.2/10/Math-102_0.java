public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
if ((expected.length < 2) || (expected.length != observed.length)) {
throw new IllegalArgumentException(
"observed, expected array lengths incorrect");
}
if (!isPositive(expected) || !isNonNegative(observed)) {
throw new IllegalArgumentException(
"observed counts must be non-negative and expected counts must be postive");
}
double sumSq = 0.0d;
for (int i = 0; i < observed.length; i++) {
    if (expected[i] == 0) {
        throw new IllegalArgumentException("Expected count cannot be zero.");
    }
    double dev = ((double) observed[i] - expected[i]);
    sumSq += Math.abs(dev * dev / expected[i]); // changed to abs() to prevent underflow
}
return sumSq;
}