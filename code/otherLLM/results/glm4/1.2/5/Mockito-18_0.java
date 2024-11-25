Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class || type == AbstractList.class) {
        return new LinkedList<Object>(); // Using LinkedList since it is a List implementation
    } else if (type == Set.class || type == HashSet.class || type == AbstractSet.class) {
        return new HashSet<Object>(); // Using HashSet since it is a Set implementation
    } else if (type == SortedSet.class || type == TreeSet.class || type == SortedMap.class || type == TreeMap.class) {
        return new TreeSet<Object>(); // Using TreeSet as it can be used for both SortedSet and SortedMap
    } else if (type == LinkedHashSet.class || type == AbstractSet.class && type.isAssignableFrom(LinkedHashSet.class)) {
        return new LinkedHashSet<Object>(); // Check if the class is LinkedHashSet or a subclass of Set
    } else if (type == Map.class) {
        return new HashMap<Object, Object>();
    } else if (type == AbstractMap.class && type.isAssignableFrom(HashMap.class)) {
        return new HashMap<Object, Object>(); // Check if the class is HashMap or a subclass of Map
    }
    // Let's not care about the rest of collections.
    return null;
}