public double getPct(Object v) {
    if (v instanceof Long || v instanceof Integer) {
        return getCumPct(Long.class.cast(v));
    } else if (v instanceof String) {
        return getCumPct(String.class.cast(v));
    } else {
        return 0; // or throw an exception, depending on the desired behavior
    }
}