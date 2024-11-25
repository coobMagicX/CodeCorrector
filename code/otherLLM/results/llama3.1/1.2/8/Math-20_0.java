public double[] repairAndDecode(final double[] x) {
    if (boundaries == null) {
        return x;
    }
    return decode(x);
}