public void addValue(Object v) {
    if (v instanceof Comparable) {
        @SuppressWarnings("unchecked")
        Comparable<Object> comparable = (Comparable<Object>) v;
        // Assuming 'collection' is the structure you want to manage. Replace with actual collection name.
        collection.add(comparable);
    } else {
        throw new IllegalArgumentException("The object does not implement Comparable interface.");
    }
}