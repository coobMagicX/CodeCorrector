public void addValue(Comparable<?> v) {
    freqTable.put(v, freqTable.getOrDefault(v, 0) + 1);
}