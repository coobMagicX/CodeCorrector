import java.util.TreeMap;
import java.util.Comparator;

public class FrequencyTable {
    private TreeMap<Object, Long> freqTable;
    private Comparator<Object> comparator;

    public FrequencyTable(Comparator<Object> comp) {
        this.comparator = comp;
        this.freqTable = new TreeMap<>(comp);
    }

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
            obj = Long.valueOf(((Integer) v).longValue());
        }

        // Retrieve existing count using the comparator for proper comparison
        Long count = freqTable.get(obj);
        if (count == null) {
            freqTable.put(obj, Long.valueOf(1));
        } else {
            // Increment the frequency count safely as long as the object is comparable
            freqTable.put(obj, Long.valueOf(count.longValue() + 1));
        }
    }
}