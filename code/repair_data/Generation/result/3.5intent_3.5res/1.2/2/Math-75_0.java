public double getPct(Object v) {
    if (v instanceof Integer) {
        return getCumPct((Comparable<?>) Integer.valueOf((int) v));
    } else {
        return getCumPct((Comparable<?>) v);
    }
}