<M extends Map<String, String>> M putIn(final M map, final CSVRecord record) {
    for (final Entry<String, Integer> entry : mapping.entrySet()) {
        final int col = entry.getValue().intValue();
        map.put(entry.getKey(), record.get(col));
    }
    return map;
}

public Map<String, String> toMap(CSVRecord record) {
    return putIn(new HashMap<String, String>(record.size()), record);
}