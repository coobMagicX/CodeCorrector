import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FrequencyDistribution {
    private Map<Comparable<?>, Integer> freqTable;

    public FrequencyDistribution() {
        freqTable = new HashMap<>();
    }

    public void addValue(Comparable<?> value) {
        freqTable.put(value, freqTable.getOrDefault(value, 0) + 1);
    }

    public int getCount(Comparable<?> value) {
        return freqTable.getOrDefault(value, 0);
    }

    public double getPct(Object v) {
        return getCumPct((Comparable<?>) v);
    }

    public double getCumPct(Comparable<?> value) {
        int totalCount = freqTable.values().stream().mapToInt(Integer::intValue).sum();
        int count = getCount(value);
        double percentage = (count / (double) totalCount) * 100;
        return BigDecimal.valueOf(percentage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Iterator<Comparable<?>> valuesIterator() {
        return freqTable.keySet().iterator();
    }
}