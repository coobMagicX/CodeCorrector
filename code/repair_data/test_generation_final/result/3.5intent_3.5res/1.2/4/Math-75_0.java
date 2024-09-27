// Fixed code
public double getPct(Object v) {
    if (v instanceof Integer) {
        return getCumPct((Integer) v);
    } else if (v instanceof Long) {
        return getCumPct((Long) v);
    } else {
        return getCumPct((Comparable<?>) v);
    }
}