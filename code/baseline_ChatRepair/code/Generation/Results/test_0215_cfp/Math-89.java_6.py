public void addValue(Object v) {
    if (v instanceof Comparable) {
        addValue((Comparable<?>) v);
    } else {
        // Handle the case where v