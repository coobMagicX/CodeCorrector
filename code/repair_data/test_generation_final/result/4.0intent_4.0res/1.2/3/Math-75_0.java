public double getPct(Object v) {
    Comparable<?> comparableValue = convertToComparable(v);
    return getCumPct(comparableValue);
}

private Comparable<?> convertToComparable(Object v) {
    if (v instanceof Integer) {
        return ((Integer) v).longValue();
    } else if (v instanceof Long) {
        return (Long) v;
    } else if (v instanceof Double) {
        return BigDecimal.valueOf((Double) v);
    } else if (v instanceof Float) {
        return BigDecimal.valueOf((Float) v);
    } else if (v instanceof Short) {
        return ((Short) v).longValue();
    } else if (v instanceof Byte) {
        return ((Byte) v).longValue();
    } else if (v instanceof BigDecimal) {
        return (BigDecimal) v;
    } else {
        return (Comparable<?>) v;
    }
}