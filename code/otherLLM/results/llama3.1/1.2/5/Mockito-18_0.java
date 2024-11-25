Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == Set.class || type == List.class || type == Map.class) {
        return Collections.emptyIterator();
    } else if (type == HashSet.class) {
        return new HashSet<>();
    } else if (type == SortedSet.class) {
        return new TreeSet<>();
    } else if (type == TreeSet.class) {
        return new TreeSet<>();
    } else if (type == LinkedHashSet.class) {
        return new LinkedHashSet<>();
    } else if (type == List.class) {
        return new LinkedList<>();
    } else if (type == ArrayList.class) {
        return new ArrayList<>();
    } else if (type == Map.class) {
        return new HashMap<>();
    } else if (type == HashMap.class) {
        return new HashMap<>();
    } else if (type == SortedMap.class) {
        return new TreeMap<>();
    } else if (type == TreeMap.class) {
        return new TreeMap<>();
    } else if (type == LinkedHashMap.class) {
        return new LinkedHashMap<>();
    }
    //Let's not care about the rest of collections.
    return null;
}