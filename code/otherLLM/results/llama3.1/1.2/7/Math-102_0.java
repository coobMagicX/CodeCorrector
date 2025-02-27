public double chiSquare(double[] expected, long[] observed)
throws IllegalArgumentException {
if ((expected.length < 2) || (expected.length != observed.length)) {
throw new IllegalArgumentException(
"observed, expected array lengths incorrect");
}
if (!isNonNegative(expected) || !isPositive(observed)) {
throw new IllegalArgumentException(
"observed counts must be positive and expected counts must be non-negative");
}
double sumSq = 0.0d;
double dev = 0.0d;
for (int i = 0; i < observed.length; i++) {
dev = ((double) observed[i] - expected[i]);
sumSq += dev * dev / expected[i];
}
return sumSq;
}

private boolean isPositive(long[] array) {
for (long value : array) {
if (value <= 0) return false;
}
return true;
}

private boolean isNonNegative(double[] array) {
for (double value : array) {
if (value < 0) return false;
}
return true;
}