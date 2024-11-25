public static double linearCombination(final double[] a, final double[] b)
throws DimensionMismatchException {
final int len = a.length;
if (len != b.length) {
throw new DimensionMismatchException(len, b.length);
}

// Revert to scalar multiplication when both input arrays have only one element.

if (len == 1) {
return a[0] * b[0];
}

double[] prodHigh = new double[len];
double prodLowSum = 0;

for (int i = 0; i < len; i++) {
final double ai = a[i];
final double ca = SPLIT_FACTOR * ai;
final double aHigh = ca - (ca - ai);
final double aLow = ai - aHigh;

final double bi = b[i];
final double cb = SPLIT_FACTOR * bi;
final double bHigh = cb - (cb - bi);
final double bLow = bi - bHigh;
prodHigh[i] = ai * bi;
final double prodLow = aLow * bLow - (((prodHigh[i] -
aHigh * bHigh) -
aLow * bHigh) -
aHigh * bLow);
prodLowSum += prodLow;
}

// Rest of the code remains the same
...
}