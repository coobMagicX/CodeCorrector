public void addValue(Object v) {
    if (v instanceof Comparable<?>) {
        addValue((Comparable<?>) v);
    } else {
        // Handle or report the case where v is not Comparable