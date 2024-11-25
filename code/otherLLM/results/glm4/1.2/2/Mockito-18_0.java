Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == Collection.class) {
        return new LinkedList<Object>();
    } else if (type == Set.class) {
        return new HashSet<Object>();
    } else if (type == List.class) {
        return new LinkedList<Object>(); // Return an empty list instead of a non-empty one.
    } else if (type == Map.class) {
        return new HashMap<Object, Object>();
    }
    
    // Other types are not handled in this context.
    return null;
}