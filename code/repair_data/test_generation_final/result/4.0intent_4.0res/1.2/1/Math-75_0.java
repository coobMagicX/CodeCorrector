public double getPct(Object v) {
    if (v instanceof Number) {
        double value = ((Number) v).doubleValue();
        Iterator<Comparable<?>> it = valuesIterator();
        while (it.hasNext()) {
            Comparable<?> current = it.next();
            if (current instanceof Number && ((Number) current).doubleValue() == value) {
                return getCumPct(current);
            }
        }
    }
    return 0.0; // Returning 0.0 if no matching value is found
}