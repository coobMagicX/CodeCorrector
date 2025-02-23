import java.util.HashMap;
import java.util.Map;

public class Frequency {
    private Map<Object, Long> freqTable = new HashMap<>();

    public void addValue(Object v) {
        /**
         * Adds 1 to the frequency count for v.
         * <p>
         * If other objects have already been added to this Frequency, v does not
         * need to be comparable to those that have already been added.
         * </p>
         * 
         * @param v the value to add.
         */ 
        Long count = freqTable.get(v);
        if (count == null) {
            freqTable.put(v, 1L);
        } else {
            freqTable.put(v, count + 1);
        }
    }
}
