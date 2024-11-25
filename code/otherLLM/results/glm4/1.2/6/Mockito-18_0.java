Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (type == Collection.class) {
        return Collections.emptyList(); // Use the existing method to get an empty list
    } else if (type == Set.class) {
        return Collections.emptySet(); // Use the existing method to get an empty set
    } else if (type == HashSet.class) {
        return Collections.emptySet(); // Return an empty set for HashSet, as it implements Set
    } else if (type == SortedSet.class) {
        return Collections.emptySortedSet(); // Return an empty sorted set
    } else if (type == TreeSet.class) {
        return Collections.emptySortedSet(); // Return an empty sorted set for TreeSet, as it implements SortedSet
    } else if (type == LinkedHashSet.class) {
        return Collections.emptySet(); // Return an empty set for LinkedHashSet, as it implements Set
    } else if (type == List.class) {
        return Collections.emptyList(); // Use the existing method to get an empty list
    } else if (type == LinkedList.class) {
        return Collections.emptyList(); // Return an empty list for LinkedList, as it implements List
    } else if (type == ArrayList.class) {
        return Collections.emptyList(); // Return an empty list for ArrayList, as it implements List
    } else if (type == Map.class) {
        return Collections.emptyMap(); // Use the existing method to get an empty map
    } else if (type == HashMap.class) {
        return Collections.emptyMap(); // Return an empty map for HashMap, as it implements Map
    } else if (type == SortedMap.class) {
        return Collections.emptySortedMap(); // Return an empty sorted map
    } else if (type == TreeMap.class) {
        return Collections.emptySortedMap(); // Return an empty sorted map for TreeMap, as it implements SortedMap
    } else if (type == LinkedHashMap.class) {
        return Collections.emptyMap(); // Return an empty map for LinkedHashMap, as it implements Map
    }
    //Let's not care about the rest of collections.
    return null;
}