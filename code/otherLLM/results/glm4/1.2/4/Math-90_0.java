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
    Object keyToUse = v;
    
    // Check if the object can be compared by comparing it against an existing entry in the freqTable
    for (Object existingKey : freqTable.keySet()) {
        if (existingKey != null && !existingKey.getClass().equals(v.getClass())) {
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    }

    // If no exception was thrown, we can safely proceed with incrementing the frequency count
    Long count = (Long) freqTable.get(keyToUse);
    if (count == null) {
        freqTable.put(keyToUse, Long.valueOf(1));
    } else {
        freqTable.put(keyToUse, Long.valueOf(count.longValue() + 1));
    }
}