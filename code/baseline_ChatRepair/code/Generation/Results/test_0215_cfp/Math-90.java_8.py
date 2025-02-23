import java.util.HashMap;

// Assuming freqTable is declared as a HashMap
private HashMap<Object, Long> freqTable = new HashMap<>();

public void addValue(Object v) {
    /**
     * Adds 1 to the frequency count for v.
     * 
     * @param v the value to add
     */
    
    // Preparing the object for counting in frequency map
    Object obj = v;

    // Handling Integer as special case to store as Long
    if (v instanceof Integer) {
        obj = Long.valueOf(((Integer)v).longValue());
    }

    // Fetch the current count for the object from freqTable
    Long count = freqTable.get(obj);

    // If the object is not already in the map, initialize its count at 1
    if (count == null) {
        freqTable.put(obj, 1L);
    } else {
        // Otherwise, increment the existing count
        freqTable.put(obj, count + 1);
    }
}
