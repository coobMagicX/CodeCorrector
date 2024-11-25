public double getPct(Object v) {
    if (v instanceof Number || v instanceof Comparable<?>) {
        return getCumPct((Comparable<?>) v);
    } else {
        throw new IllegalArgumentException("Input must be a numeric value or an object with a comparable type");
    }
}