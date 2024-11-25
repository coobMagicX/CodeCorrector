public double getPct(Object v) {
    if (v instanceof Number) {
        return getCumPct((Comparable<?>) v);
    } else {
        throw new IllegalArgumentException("Input must be a numeric value");
    }
}