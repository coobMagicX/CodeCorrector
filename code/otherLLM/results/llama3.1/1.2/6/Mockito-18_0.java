Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else {
        return Collections.empty[type.newInstance().getClass()];
    }
}