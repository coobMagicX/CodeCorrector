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
        // Convert Integer to Long to ensure compatibility with all long values
        obj = Long.valueOf(((Integer) v).longValue());
    }

    // Check for comparability before trying to retrieve or update the count
    Comparable comparableObj = null;
    if (obj instanceof Comparable) {
        comparableObj = (Comparable) obj;
    } else {
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }

    try {
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            // Increment the frequency count
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {
        // TreeMap will throw ClassCastException if v is not comparable to previous entries
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}