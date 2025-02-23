import java.util.HashMap;
import java.util.Map;

public class Frequency {
    private Map<Object, Long> freqTable = new HashMap<>();

    public void addValue(Object v) {
        Object obj = v;
        if (v instanceof Integer) {
            obj = Long.valueOf(((Integer) v).longValue());
        }
        
        Long count = freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            freqTable.put(obj, Long.valueOf(count + 1));
        }
    }
}

