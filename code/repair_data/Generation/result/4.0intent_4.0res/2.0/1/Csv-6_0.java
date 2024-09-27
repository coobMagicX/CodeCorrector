import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class CsvRecord {
    private String[] values;
    private Map<String, Integer> mapping;

    public CsvRecord(String[] values, Map<String, Integer> mapping) {
        this.values = values;
        this.mapping = mapping;
    }

    // Utility method to safely get value from values array
    private String safeGetValue(int index) {
        if (index < values.length) {
            return values[index];
        } else {
            return null; // Return null if index is out of bounds
        }
    }

    // Modified putIn method to handle records with fewer fields
    public <M extends Map<String, String>> M putIn(final M map) {
        for (final Entry<String, Integer> entry : mapping.entrySet()) {
            final int col = entry.getValue().intValue();
            map.put(entry.getKey(), safeGetValue(col)); // Use safeGetValue to prevent IndexOutOfBoundsException
        }
        return map;
    }

    // Method to convert values to a Map based on the header mapping
    public Map<String, String> toMap() {
        return putIn(new HashMap<String, String>(values.length));
    }
}