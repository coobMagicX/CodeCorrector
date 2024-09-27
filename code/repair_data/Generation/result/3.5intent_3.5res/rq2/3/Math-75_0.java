public double getPct(Object v) {
    if (v instanceof Integer) {
        return getCumPct((Integer) v);
    } else if (v instanceof Long) {
        return getCumPct((Long) v);
    } else {
        return getCumPct((Comparable<?>) v);
    }
}

private double getCumPct(Integer v) {
    // Implementation for calculating percentage for Integer objects
}

private double getCumPct(Long v) {
    // Implementation for calculating percentage for Long objects
}

private double getCumPct(Comparable<?> v) {
    // Implementation for calculating percentage for other types of objects
}