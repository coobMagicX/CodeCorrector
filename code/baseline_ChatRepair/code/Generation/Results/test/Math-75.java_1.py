public double getPct(Object v) {
    if (v instanceof Comparable) {
        return getCumPct((Comparable<?>) v);
    } else {
        throw new IllegalArgumentException("The provided object is not comparable.");
    }
}
