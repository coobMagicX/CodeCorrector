Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class) {
        // Return an empty instance of Collection
        return Collections.emptyList();
    } else if (type == Set.class || type == HashSet.class || type == LinkedHashSet.class) {
        // Return an empty instance of Set or its subclass
        return new HashSet<>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        // Return an empty instance of SortedSet or its subclass
        return new TreeSet<>();
    } else if (type == List.class || type == LinkedList.class || type == ArrayList.class) {
        // Return an empty instance of List or its subclass
        return Collections.emptyList();
    } else if (type == Map.class || type == HashMap.class || type == LinkedHashMap.class || type == TreeMap.class) {
        // Return an empty instance of Map or its subclass
        return new HashMap<>();
    }
    return null;
}