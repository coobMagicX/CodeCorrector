import java.util.HashMap;

// Assuming freqTable is a HashMap now
private HashMap<Object, Long> freqTable = new HashMap<>();

public void addValue(Object v) {
    /**
     * Adds 1 to the frequency count for v.
     * 
     * @param v the value to add
     */
    Object obj = v;

    // Convert Integer to Long to maintain consistent data types
    if (v instanceof Integer) {
       obj = Long.valueOf(((Integer) v).longValue());
    }

    // Increase count in freqTable without assuming Comparable
    Long count = freqTable.get(obj);
    if (count == null) {
        freqTable.put(obj, 1L);  // Simplified creation of Long objects
    } else {
        freqTable.put(obj, count + 1L);  // Simplified increment
    }
}
