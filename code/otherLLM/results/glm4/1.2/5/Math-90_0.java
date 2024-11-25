public void addValue(Object v) {

    /**
     * Adds 1 to the frequency count for v.
     * <p>
     * If other objects have already been added to this Frequency, v must
     * be comparable to those that have already been added.
     * </p>
     *
     * @param v the value to add.
     * @throws IllegalArgumentException if <code>v</code> is not comparable with previous entries
     */
    Object obj = v;
    if (v instanceof Integer) {
        // Convert Integer to Long to ensure comparability.
        obj = Long.valueOf(((Integer) v).longValue());
    }

    // Before adding or updating the count, check if the object is Comparable.
    if (!(obj instanceof Comparable)) {
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }

    try {
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            // Increment the count for this object.
            long newCount = count.longValue() + 1;
            freqTable.put(obj, Long.valueOf(newCount));
        }
    } catch (ClassCastException ex) {
        // TreeMap will throw ClassCastException if v is not comparable
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}