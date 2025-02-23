public void addValue(Object v) {
    if (v instanceof Comparable<?>) {
        addValue((Comparable<?>) v);
    } else {
        // You can take appropriate action here such as throwing an exception
