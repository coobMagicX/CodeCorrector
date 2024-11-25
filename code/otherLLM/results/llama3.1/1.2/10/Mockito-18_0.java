Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || type == List.class || type == Set.class) {
        return Collections.emptyList();
    } else if (type == Map.class) {
        return Collections.emptyMap();
    } else if (type == HashSet.class || type == SortedSet.class || type == TreeSet.class 
               || type == LinkedHashSet.class || type == LinkedList.class 
               || type == ArrayList.class || type == HashMap.class 
               || type == SortedMap.class || type == TreeMap.class 
               || type == LinkedHashMap.class) {
        return new instanceOf(type);
    }
    //Let's not care about the rest of collections.
    return null;
}

class instanceOf<T> extends Collection<T>, Map<T, T> {
    public instanceOf(Class<?> type) {}
}