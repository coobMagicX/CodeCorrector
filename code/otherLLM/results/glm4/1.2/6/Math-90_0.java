import java.util.TreeMap;
import java.util.Comparator;

public void addValue(Object v) {
    // Check if the frequency table is empty, and set a default comparator
    if (freqTable.isEmpty()) {
        freqTable = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
    }

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
    Object keyToUse;

    // Check if the object can be wrapped into a Long, which is Comparable
    if (v instanceof Integer) {
        keyToUse = Long.valueOf(((Integer) v).longValue());
    } else if (v == null) {
        keyToUse = null; // Handle null values by using null as the key
    } else {
        throw new IllegalArgumentException("Cannot add non-comparable value: " + v);
    }

    // Proceed with adding to the frequency table only if the key is valid
    Long count = (Long) freqTable.get(keyToUse);

    if (count == null) {
        freqTable.put(keyToUse, Long.valueOf(1));
    } else {
        freqTable.put(keyToUse, Long.valueOf(count.longValue() + 1));
    }
}

// Assuming the existence of a TreeMap named 'freqTable' which is used to store frequencies
private TreeMap<Object, Long> freqTable;