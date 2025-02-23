import java.util.HashMap;
import java.util.Map;

public class Frequency {
    // Using a HashMap to store frequencies
    private Map<Object, Long> freqTable = new HashMap<>();

    public void addValue(Object v) {
        /**
         * Adds 1 to the frequency count for v.
         * @param v the value to add.
         */
        
        // Get current count for the value 'v' from frequency table
        Long count = freqTable.get(v);
        if (count == null) {
            // If no count exists, initialize to 1
            freqTable.put(v, 1L);
        } else {
            // If a count exists, increment it by 1
            freqTable.put(v, count + 1);
        }
    }
}
