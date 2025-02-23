import java.util.HashMap;
import java.util.Map;

public class Frequency {
    private Map<Object, Long> freqTable;

    public Frequency() {
        freqTable = new HashMap<>();
    }

    public void addValue(Object v) {
        // Convert integers to long to unify number types
        Object obj = (v instanceof Integer) ? Long.valueOf(((Integer) v).longValue()) : v;
        
        // Fetch the current count from the map
        Long count = freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, 1L);  // If it's not in the map, start the count at 1
        } else {
            freqTable.put(obj, count + 1);  // Otherwise, increment the existing count
        }
    }
}
