Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == Collection.class) {
        return Collections.emptyList();
    } else if (type == Set.class) {
        return new HashSet<Object>();
    } else if (type == List.class) {
        return new ArrayList<Object>();
    } else if (type == Map.class) {
        return new HashMap<Object, Object>();
    } else {
        //For other collection types, we can use the existing method to create an empty instance
        return returnValueFor(type);
    }
}