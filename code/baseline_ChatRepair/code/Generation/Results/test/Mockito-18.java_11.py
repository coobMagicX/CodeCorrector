Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class || type == LinkedList.class) {
        return new LinkedList<Object>();
    } else if (type == Set.class) {
        return new HashSet<Object>();
    } else if (type == HashSet.class || type == LinkedHashSet.class) {
        return new HashSet<Object>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<Object>();
    } else if (type == ArrayList.class) {
        return new ArrayList<Object>();
    } else if (type == Map.class || type == HashMap.class || type == LinkedHashMap.class) {
        return new HashMap<Object, Object>();
    } else if (type == SortedMap.class || type == TreeMap.class) {
        return new TreeMap<Object, Object>();
    } else if (type == Iterable.class) {
        // Specifically handling Iterable to return an empty iterable
        return new ArrayList<Object>();
    }
    // For any other types not explicitly handled
    return null;
}
