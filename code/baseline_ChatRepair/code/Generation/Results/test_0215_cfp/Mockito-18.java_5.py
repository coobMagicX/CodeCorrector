Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class || type == Iterable.class) {
        return new ArrayList<Object>();
    } else if (type == Set.class || type == HashSet.class) {
        return new HashSet<Object>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<Object>();
    } else if (type == LinkedHashSet.class) {
        return new LinkedHashSet<Object>();
    } else if (type == LinkedList.class) {
        return new LinkedList<Object>();
    } else if (type == ArrayList.class) {
        return new ArrayList<Object>();
    } else if (type == Map.class || type == HashMap.class) {
        return new HashMap<Object, Object>();
    } else if (type == SortedMap.class || type == TreeMap.class) {
        return new TreeMap<Object, Object>();
    } else if (type == LinkedHashMap.class) {
        return new LinkedHashMap<Object, Object>();
    }
    
    // In any unexpected or null type cases, return an empty ArrayList which is an object of Iterable.
    // This prevents NullPointerException when the method's output is used in Iterable context.
    return new ArrayList<Object>();
}
