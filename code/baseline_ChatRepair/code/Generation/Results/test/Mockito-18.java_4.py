Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class) {
        return new ArrayList<Object>();
    } else if (type == Set.class) {
        return new HashSet<Object>();
    } else if (type == HashSet.class) {
        return new HashSet<Object>();
    } else if (type == SortedSet.class) {
        return new TreeSet<Object>();
    } else if (type == TreeSet.class) {
        return new TreeSet<Object>();
    } else if (type == LinkedHashSet.class) {
        return new LinkedHashSet<Object>();
    } else if (type == LinkedList.class) {
        return new LinkedList<Object>();
    } else if (type == ArrayList.class) {
        return new ArrayList<Object>();
    } else if (type == Map.class) {
        return new HashMap<Object, Object>();
    } else if (type == HashMap.class) {
        return new HashMap<Object, Object>();
    } else if (type == SortedMap.class) {
        return new TreeMap<Object, Object>();
    } else if (type == TreeMap.class) {
        return new TreeMap<Object, Object>();
    } else if (type == LinkedHashMap.class) {
        return new LinkedHashMap<Object, Object>();
    } else if (type == Iterable.class) {
        // Handle Iterable type specifically, returning an empty ArrayList which is an Iterable
        return new ArrayList<Object>();
    }
    // Let's not care about the rest of collections.
    return null;
}
