Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == Collection.class || type == List.class) {
        return new LinkedList<Object>();
    } else if (type == Set.class || type == HashSet.class || type == LinkedHashSet.class) {
        return new HashSet<Object>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<Object>();
    } else if (type == Map.class || type == HashMap.class || type == TreeMap.class || type == LinkedHashMap.class) {
        return new HashMap<Object, Object>();
    }
    //Let's not care about the rest of collections.
    return null;
}