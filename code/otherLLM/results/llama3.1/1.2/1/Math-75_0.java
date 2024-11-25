public double getPct(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Input object must be comparable to others");
    }
    return getCumPct((Comparable<?>) v);
}