public void addValue(Object v) {
    Long count = (Long) freqTable.get(v);
    if (count == null) {
        freqTable.put(v, 1L);  // Make use of autoboxing to simplify the code (Long.valueOf(1) can be expressed as 1L).
    } else {
        freqTable.put(v, count + 1);  // Similary, use count + 1 to leverage autoboxing.
    }
}
