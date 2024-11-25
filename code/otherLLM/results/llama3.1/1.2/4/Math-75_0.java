public double getPct(Object v) {
    if (!(v instanceof Number)) {
        throw new IllegalArgumentException("Input 'v' must be a numeric type");
    }
    return getCumPct((Number) v);
}