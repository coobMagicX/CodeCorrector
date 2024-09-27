<M extends Map<String, String>> M putIn(final M map) {
    for (final Entry<String, Integer> entry : mapping.entrySet()) {
        final int col = entry.getValue().intValue();
        if (col >= 0 && col < values.length) {
            map.put(entry.getKey(), values[col]);
        } else {
            map.put(entry.getKey(), "");
        }
    }
    return map;
}