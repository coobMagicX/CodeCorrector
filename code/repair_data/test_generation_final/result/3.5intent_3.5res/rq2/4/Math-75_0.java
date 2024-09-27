public double getPct(Object v) {
    if (v instanceof Integer || v instanceof Long) {
        return getCumPct(((Number) v).doubleValue());
    } else {
        return getCumPct((Comparable<?>) v);
    }
}