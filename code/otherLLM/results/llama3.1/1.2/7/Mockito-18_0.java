Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (Collection.class.isAssignableFrom(type)) {
        return Collections.emptyList();
    } else if (Map.class.isAssignableFrom(type)) {
        return new HashMap<Object, Object>();
    } else {
        // Handle Iterable class correctly
        if (Iterable.class.isAssignableFrom(type)) {
            return Collections.emptyIterator();
        }
        return null;
    }
}