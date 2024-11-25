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
       // Removed unnecessary casting logic
       obj = ((Integer) v).longValue();
    }
    try {
        Long count = (Long) freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count + 1)); // Removed unnecessary casting
        }
    } catch (ClassCastException ex) {   
        //TreeMap will throw ClassCastException if v is not comparable
        if (!(v instanceof Comparable)) {
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    }
}