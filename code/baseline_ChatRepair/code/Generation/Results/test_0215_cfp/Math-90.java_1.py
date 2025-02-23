public void addValue(Object v) {
    // Using HashMap allows any object type to be added without casting issues.
    Long count = (Long) freqTable.get(v);
    if (count == null) {
        freqTable.put(v, Long.valueOf(1));
    } else {
        freqTable.put(v, Long.valueOf(count.longValue() + 1));
    }
}
