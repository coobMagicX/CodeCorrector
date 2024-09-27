Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
        //new instances are used instead of Collections.emptyList(), etc.
        //to avoid UnsupportedOperationException if code under test modifies returned collection
    } else if (Iterable.class.isAssignableFrom(type)) {
        return new ArrayList<Object>();
    } else if (Collection.class.isAssignableFrom(type)) {
        return new LinkedList<Object>();
    } else if (Set.class.isAssignableFrom(type)) {
        return new HashSet<Object>();
    } else if (HashSet.class.isAssignableFrom(type)) {
        return new HashSet<Object>();
    } else if (SortedSet.class.isAssignableFrom(type)) {
        return new TreeSet<Object>();
    } else if (TreeSet.class.isAssignableFrom(type)) {
        return new TreeSet<Object>();
    } else if (LinkedHashSet.class.isAssignableFrom(type)) {
        return new LinkedHashSet<Object>();
    } else if (List.class.isAssignableFrom(type)) {
        return new LinkedList<Object>();
    } else if (LinkedList.class.isAssignableFrom(type)) {
        return new LinkedList<Object>();
    } else if (ArrayList.class.isAssignableFrom(type)) {
        return new ArrayList<Object>();
    } else if (Map.class.isAssignableFrom(type)) {
        return new HashMap<Object, Object>();
    } else if (HashMap.class.isAssignableFrom(type)) {
        return new HashMap<Object, Object>();
    } else if (SortedMap.class.isAssignableFrom(type)) {
        return new TreeMap<Object, Object>();
    } else if (TreeMap.class.isAssignableFrom(type)) {
        return new TreeMap<Object, Object>();
    } else if (LinkedHashMap.class.isAssignableFrom(type)) {
        return new LinkedHashMap<Object, Object>();
    }
    //Let's not care about the rest of collections.
    return null;
}