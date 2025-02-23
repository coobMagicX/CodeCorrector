public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Value is not comparable");
    }

    // Ensures the use of a generalized comparable type for all stored values.
    Comparable<Object> obj = (Comparable<Object>) v;
    try {
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {
        // This block might no longer be necessary if all inserted elements are ensured to be Comparable
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}
