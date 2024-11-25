import java.util.TreeMap;

public class FrequencyTable {
    private TreeMap<Object, Long> freqTable = new TreeMap<>();

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
        if (v == null) {
            throw new IllegalArgumentException("Null values cannot be added.");
        }

        try {
            Long count = freqTable.get(v);
            if (count == null) {
                freqTable.put(v, 1L);
            } else {
                freqTable.put(v, count + 1);
            }
        } catch (IllegalArgumentException ex) {
            // TreeMap will throw IllegalArgumentException if v is not comparable
            throw new IllegalArgumentException("Value not comparable to existing values.", ex);
        }
    }
}