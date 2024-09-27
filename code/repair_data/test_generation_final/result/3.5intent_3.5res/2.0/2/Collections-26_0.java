private Object readResolve() {
    calculateHashCode(keys);
    return this;
}

private void calculateHashCode(final Object[] keys) {
    int total = 0;
    for (final Object key : keys) {
        if (key != null) {
            total ^= key.hashCode();
        }
    }
    hashCode = total;
}