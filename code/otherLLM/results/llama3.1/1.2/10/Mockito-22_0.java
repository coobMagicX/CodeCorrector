public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) ? areArraysEqual(o1, o2) : false;
    } else if (isBadEquals(o1.getClass()) && isBadEquals(o2.getClass())) {
        return badEquals(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

private static boolean isArray(Object obj) {
    return obj instanceof Object[] || obj instanceof int[] || obj instanceof float[];
}

private static boolean areArraysEqual(Object arr1, Object arr2) {
    if (!isArray(arr1) || !isArray(arr2)) {
        return false;
    }
    
    if (arr1.getClass() != arr2.getClass()) {
        return false;
    }
    
    int len1 = ((Object[])arr1).length;
    int len2 = ((Object[])arr2).length;
    
    if (len1 != len2) {
        return false;
    }
    
    for (int i = 0; i < len1; i++) {
        if (!((Object[])arr1)[i].equals(((Object[])arr2)[i])) {
            return false;
        }
    }
    
    return true;
}

private static boolean isBadEquals(Class<?> clazz) {
    return BadEquals.class.isAssignableFrom(clazz);
}

private static boolean badEquals(Object o1, Object o2) {
    // Assuming BadEquals has a static method equals
    return BadEquals.equals(o1, o2);
}