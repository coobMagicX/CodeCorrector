public static double linearCombination(final double[] a, final double[] b)
throws DimensionMismatchException {
final int len = a.length;
if (len != b.length) {
throw new DimensionMismatchException(len, b.length);
}

// Handle edge case where one or both input arrays have a single element
if (len == 1) {
return a[0] * b[0];
}

// Revert to naive implementation for multi-element arrays
double result = 0;
for (int i = 0; i < len; ++i) {
result += a[i] * b[i];
}
return result;
}