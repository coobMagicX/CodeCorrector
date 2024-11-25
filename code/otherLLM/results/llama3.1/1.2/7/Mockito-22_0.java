public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (!isArray(o2)) {
            return false; // incompatible types
        }
        return areArraysEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

static boolean areArrayLengthsEqual(Object o1, Object o2) {
    if (o1 instanceof int[] && o2 instanceof int[]) {
        int[] arr1 = (int[]) o1;
        int[] arr2 = (int[]) o2;
        return arr1.length == arr2.length;
    } else if (o1 instanceof double[] && o2 instanceof double[]) {
        double[] arr1 = (double[]) o1;
        double[] arr2 = (double[]) o2;
        return arr1.length == arr2.length;
    } else if (o1 instanceof Object[] && o2 instanceof Object[]) {
        Object[] arr1 = (Object[]) o1;
        Object[] arr2 = (Object[]) o2;
        return arr1.length == arr2.length;
    }
    // Add more type-specific checks as needed
    return false; // unknown array types
}

static boolean areArrayElementsEqual(Object o1, Object o2) {
    if (o1 instanceof int[] && o2 instanceof int[]) {
        int[] arr1 = (int[]) o1;
        int[] arr2 = (int[]) o2;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    } else if (o1 instanceof double[] && o2 instanceof double[]) {
        double[] arr1 = (double[]) o1;
        double[] arr2 = (double[]) o2;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    } else if (o1 instanceof Object[] && o2 instanceof Object[]) {
        Object[] arr1 = (Object[]) o1;
        Object[] arr2 = (Object[]) o2;
        for (int i = 0; i < arr1.length; i++) {
            if (!arr1[i].equals(arr2[i])) {
                return false;
            }
        }
        return true;
    }
    // Add more type-specific checks as needed
    return false; // unknown array types
}