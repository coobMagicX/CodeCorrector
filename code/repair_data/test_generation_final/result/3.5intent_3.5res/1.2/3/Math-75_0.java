public double getPct(Object v) {
    if (v instanceof Integer) {
        return getCumPct((Comparable<?>) ((Integer) v).doubleValue());
    } else if (v instanceof Long) {
        return getCumPct((Comparable<?>) ((Long) v).doubleValue());
    } else {
        return getCumPct((Comparable<?>) v);
    }
}