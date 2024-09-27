Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class) {
        return new LinkedList<>();
    } else if (type == Set.class || type == HashSet.class) {
        return new HashSet<>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<>();
    } else if (type == LinkedHashSet.class) {
        return new LinkedHashSet<>();
    } else if (type == List.class || type == LinkedList.class || type == ArrayList.class) {
        return new LinkedList<>();
    } else if (type == Map.class || type == HashMap.class) {
        return new HashMap<>();
    } else if (type == SortedMap.class || type == TreeMap.class) {
        return new TreeMap<>();
    } else if (type == LinkedHashMap.class) {
        return new LinkedHashMap<>();
    }
    //Let's not care about the rest of collections.
    return null;
}