public void addValue(Object v) {
    /**
     * Adds 1 to the frequency count for v.
     * <p>
     * If other objects have already been added to this Frequency, v must be comparable to those that have already been added.
     * </p>
     * 
     * @param v the value to add.
     */
     
    // Ensure frequencies are counted using a HashMap to avoid any comparability issues.
    Long count = (Long) freqTable.get(v);
    if (count == null) {
        freqTable.put(v, Long.valueOf(1));
    } else {
        freqTable.put(v, Long.valueOf(count.longValue() + 1));
    }
}
