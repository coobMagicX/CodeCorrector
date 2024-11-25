Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        // new instances are used instead of Collections.emptyList(), etc.
        // to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == Iterable.class) {
        return Collections.emptyList(); // Return an empty iterable for Iterable class
    } else if (type == Collection.class) {
        return new LinkedList<Object>();
    } else if (type == Set.class) {
        return new HashSet<Object>();
    } else if (type == HashSet.class) {
        return new HashSet<Object>(); // This line is redundant and can be removed as it duplicates the previous if
    } else if (type == SortedSet.class) {
        return new TreeSet<Object>();
    } else if (type == TreeSet.class) {
        return new TreeSet<Object>(); // This line is redundant and can be removed as it duplicates the previous if
    } else if (type == LinkedHashSet.class) {
        return new LinkedHashSet<Object>();
    } else if (type == List.class) {
        return new LinkedList<Object>();
    } else if (type == LinkedList.class) {
        return new LinkedList<Object>(); // This line is redundant and can be removed as it duplicates the previous if
    } else if (type == ArrayList.class) {
        return new ArrayList<Object>();
    } else if (type == Map.class) {
        return new HashMap<Object, Object>();
    } else if (type == HashMap.class) {
        return new HashMap<Object, Object>(); // This line is redundant and can be removed as it duplicates the previous if
    } else if (type == SortedMap.class) {
        return new TreeMap<Object, Object>();
    } else if (type == TreeMap.class) {
        return new TreeMap<Object, Object>(); // This line is redundant and can be removed as it duplicates the previous if
    } else if (type == LinkedHashMap.class) {
        return new LinkedHashMap<Object, Object>();
    }
    // The rest of collections do not need to be handled in this method.
    return null;
}