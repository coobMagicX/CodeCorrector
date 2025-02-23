public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        // Handle non-comparable objects or simply skip / log / throw an exception
        throw new IllegalArgumentException("Value must be comparable.");
    }

    Object obj = v;
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer) v).longValue());
    }
    try {
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {
        // Even though we check for comparable, this is kept as an additional sanity catch.
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}
