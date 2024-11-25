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
    // Ensure that v is an instance of Comparable before proceeding
    if (v instanceof Comparable<?>) {
        Object obj = v;
        if (v instanceof Integer) {
            obj = Long.valueOf(((Integer) v).longValue());
        }
        try {
            Long count = (Long) freqTable.get(obj);
            if (count == null) {
                freqTable.put(obj, Long.valueOf(1));
            } else {
                // Safely increment the count by converting to Long again
                freqTable.put(obj, Long.valueOf(count.longValue() + 1));
            }
        } catch (ClassCastException ex) {   
            // TreeMap will throw ClassCastException if v is not comparable
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    } else {
        throw new IllegalArgumentException("The value must implement Comparable interface");
    }
}