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
    // Type casting to Long, but only if v is an Integer or a Character (which can be autoboxed to Integer)
    if (v instanceof Integer || v instanceof Character) {
        obj = Long.valueOf(((Number) v).longValue());
    }

    try {
        // Use the `Long` instance for comparison and access in the map
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    } catch (ClassCastException ex) {   
        // TreeMap will throw ClassCastException if v is not comparable
        throw new IllegalArgumentException("Value not comparable to existing values.");
    }
}