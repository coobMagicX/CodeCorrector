Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class || type == LinkedList.class) {
        return new LinkedList<>();
    } else if (type == Set.class) {
        return new HashSet<>();
    } else if (type == HashSet.class || type == LinkedHashSet.class) {
        return new HashSet<>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<>();
    } else if (type == ArrayList.class) {
        return new ArrayList<>();
    } else if (type == Map.class || type == HashMap.class || type == LinkedHashMap.class) {
        return new HashMap<>();
    } else if (type == SortedMap.class || type == TreeMap.class) {
        return new TreeMap<>();
    } else if (type == Iterable.class) {
        // Specifically handling Iterable to return an empty iterable
        return Collections.emptyList();
    }
    // For any other types not explicitly handled
    return null;
}
