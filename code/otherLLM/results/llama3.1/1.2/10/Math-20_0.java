public double[] repairAndDecode(final double[] x) {
    final double lower = -3.0;
    final double upper = 4.5; // Maximum valid value for y within the given bounds
    final double decoded = decode(x);
    return Math.min(Math.max(decoded, lower), upper); // Apply bounds to decoded value
}