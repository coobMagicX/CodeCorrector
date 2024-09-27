public Object answer(InvocationOnMock invocation) {
    if (methodsGuru.isToString(invocation.getMethod())) {
        Object mock = invocation.getMock();
        MockName name = mockUtil.getMockName(mock);
        if (name.isDefault()) {
            return "Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        } else {
            return name.toString();
        }
    } else if (methodsGuru.isCompareToMethod(invocation.getMethod())) {
        //see issue 184.
        //mocks by default should return 0 if references are the same, otherwise some other value because they are not the same. Hence we return 1 (anything but 0 is good).
        //Only for compareTo() method by the Comparable interface
        return 1;
    }
    
    Class<?> returnType = invocation.getMethod().getReturnType();
    return returnValueFor(returnType);
}

Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class) {
        return new LinkedList<Object>();
    } else if (type == Set.class || type == HashSet.class || type == LinkedHashSet.class) {
        return new HashSet<Object>();
    } else if (type == SortedSet.class || type == TreeSet.class) {
        return new TreeSet<Object>();
    } else if (type == List.class || type == LinkedList.class || type == ArrayList.class) {
        return new LinkedList<Object>();
    } else if (type == Map.class || type == HashMap.class || type == LinkedHashMap.class) {
        return new HashMap<Object, Object>();
    } else if (type == SortedMap.class || type == TreeMap.class) {
        return new TreeMap<Object, Object>();
    }
    // TODO return empty Iterable ; see issue 175

    //Let's not care about the rest of collections.
    return null;
}