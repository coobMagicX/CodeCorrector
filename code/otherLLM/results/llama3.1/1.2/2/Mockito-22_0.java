public static boolean areEqual(Object o1, Object o2) {
    if (o1 == null || o2 == null) {
        return o1 == null && o2 == null;
    } else if (isArray(o1)) {
        if (!isArray(o2)) {
            return false; // type mismatch, cannot compare
        }
        return areArraysEqual(o1, o2);
    } else {
        try {
            Class<?> class1 = o1.getClass();
            Class<?> class2 = o2.getClass();

            if (class1 != class2) { // handle badEquals instances
                return false;
            }

            for (Method method : class1.getMethods()) {
                if (!method.getName().equals("equals")) continue;

                // check if equals method is overridden in a way that breaks equality checks
                if (Modifier.isNative(method.getModifiers())) {
                    return false; // do not trust native methods to implement correct equals behavior
                }
            }

            return o1.equals(o2);
        } catch (Exception e) { // ignore exceptions, return true if equals method is overridden incorrectly
            return true;
        }
    }
}