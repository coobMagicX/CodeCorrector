import java.util.Map;
import java.util.TreeMap;

public class FrequencyTable {

    private Map<Object, Long> freqTable;

    public FrequencyTable() {
        freqTable = new TreeMap<>();
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
    public void addValue(Object v) {
        // Check if obj is already present in the frequency table or can be compared with existing keys
        Object comparableKey = getComparableKey(v);
        try {
            Long count = freqTable.get(comparableKey);
            if (count == null) {
                freqTable.put(comparableKey, 1L); // Use Long.valueOf(1) here for clarity
            } else {
                freqTable.put(comparableKey, count + 1); // Perform arithmetic to increment the count
            }
        } catch (ClassCastException ex) {
            // TreeMap will throw ClassCastException if v is not comparable with previous entries
            throw new IllegalArgumentException("Value not comparable to existing values.");
        }
    }

    /**
     * Returns a key that can be compared with existing keys in the map. This method handles conversion of Integer
     * to Long and ensures all other types are comparable.
     *
     * @param v the value to convert to a comparable key.
     * @return A comparable key for the value or the original value if it's already comparable.
     */
    private Object getComparableKey(Object v) {
        // If the value is an Integer, we need to make sure it can be compared with existing keys
        if (v instanceof Integer) {
            return Long.valueOf(((Integer) v).longValue());
        }
        // For all other types, return the original object
        return v;
    }
}