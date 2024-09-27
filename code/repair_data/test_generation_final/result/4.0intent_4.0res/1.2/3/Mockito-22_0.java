public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        return isArray(o2) && areArraysEqual(o1, o2);
    } else {
        try {
            return o1.equals(o2);
        } catch (Exception e) {
            return false; // Handling cases where equals method might throw an exception
        }
    }
}

static boolean isArray(Object o) {
    return o.getClass().isArray();
}

static boolean areArraysEqual(Object o1, Object o2) {
    if (!o1.getClass().equals(o2.getClass())) {
        return false;
    }
    if (o1 instanceof Object[] && o2 instanceof Object[]) {
        return Arrays.deepEquals((Object[]) o1, (Object[]) o2);
    } else if (o1 instanceof int[] && o2 instanceof int[]) {
        return Arrays.equals((int[]) o1, (int[]) o2);
    } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
        return Arrays.equals((byte[]) o1, (byte[]) o2);
    } else if (o1 instanceof short[] && o2 instanceof short[]) {
        return Arrays.equals((short[]) o1, (short[]) o2);
    } else if (o1 instanceof long[] && o2 instanceof long[]) {
        return Arrays.equals((long[]) o1, (long[]) o2);
    } else if (o1 instanceof char[] && o2 instanceof char[]) {
        return Arrays.equals((char[]) o1, (char[]) o2);
    } else if (o1 instanceof float[] && o2 instanceof float[]) {
        return Arrays.equals((float[]) o1, (float[]) o2);
    } else if (o1 instanceof double[] && o2 instanceof double[]) {
        return Arrays.equals((double[]) o1, (double[]) o2);
    } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
        return Arrays.equals((boolean[]) o1, (boolean[]) o2);
    } else {
        return false; // default case for arrays of unknown types
    }
}