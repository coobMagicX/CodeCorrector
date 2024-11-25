public double getPct(Object v) {
    if (v instanceof Long || v instanceof Integer) {
        // Special handling for Long and Integer values
        return getCumPct((Comparable<?>) ((Number)v).doubleValue());
    } else {
        return getCumPct((Comparable<?>) v);
    }
}