Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Iterable.class || Collection.class.isAssignableFrom(type)) {
        // Use polymorphism to create instances of different collection types
        return Collections.emptyList();
    } else if (Map.class.isAssignableFrom(type)) {
        return Collections.emptyMap();
    }
    return null;
}