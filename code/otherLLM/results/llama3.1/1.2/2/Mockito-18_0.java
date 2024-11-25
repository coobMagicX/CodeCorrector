Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class || isCollectionClass(type)) {
        return Collections.emptyList();
    } else if (isMapClass(type)) {
        return new HashMap<Object, Object>();
    }
    //Let's not care about the rest of collections.
    return null;
}

private boolean isCollectionClass(Class<?> type) {
    return type == Collection.class || type == Set.class || type == List.class
            || type == LinkedList.class || type == ArrayList.class
            || type == HashSet.class || type == TreeSet.class
            || type == LinkedHashSet.class;
}

private boolean isMapClass(Class<?> type) {
    return type == Map.class || type == HashMap.class || type == SortedMap.class
            || type == TreeMap.class || type == LinkedHashMap.class;
}