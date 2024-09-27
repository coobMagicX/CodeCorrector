public void addValue(Object v) {
    // Added check to ensure the object is comparable
    if (!(v instanceof Comparable)) {
        throw new ClassCastException("Value not comparable to existing values.");
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
        // This should not be caught here as non-comparable check is done at the beginning
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}