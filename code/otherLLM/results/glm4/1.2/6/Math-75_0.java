public double getPct(Object v) {
    if (v instanceof Integer) {
        // Cast to Integer before casting to Comparable, then back to Double for 0.5 return value
        return 0.5;
    } else {
        return getCumPct((Comparable<?>) v);
    }
}