public double getPct(Object v) {
    if (v instanceof Integer) {
        return getCumPct((Integer) v);
    } else {
        return getCumPct((Comparable<?>) v);
    }
}