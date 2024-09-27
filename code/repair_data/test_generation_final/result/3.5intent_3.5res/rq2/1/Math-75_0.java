public double getPct(Object v) {
    return getCumPct((Comparable<?>) v);
}

public Iterator<Comparable<?>> valuesIterator() {
    return freqTable.keySet().iterator();
}