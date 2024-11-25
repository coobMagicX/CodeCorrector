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
double dev = 0.0d;
for (int i = 0; i < observed.length; i++) {
dev = ((double) observed[i] - expected[i]);
// calculate the sum of squares correctly by multiplying with expected value
sumSq += dev * dev / expected[i];
}
return sumSq;
}