public double getPct(Object v) {
    if (v instanceof Character) {
        return getCumPct(v);
    }
    return getCumPct((Comparable<?>) v);
}