public EqualsBuilder append(Object lhs, Object rhs) {
    if (isEquals == false) {
        return this;
    }
    if (lhs == rhs) {
        return this;
    }
    if (lhs == null || rhs == null) {
        this.setEquals(false);
        return this;
    }
    Class lhsClass = lhs.getClass();
    if (!lhsClass.isArray()) {
        // The simple case, not an array, just test the element
        isEquals = lhs.equals(rhs);
    } else if (lhs.getClass() != rhs.getClass()) {
        // Here when we compare different dimensions, for example: a boolean[][] to a boolean[] 
        this.setEquals(false);
    }
    // 'Switch' on type of array, to dispatch to the correct handler
    // This handles multi dimensional arrays of the same depth
    else if (lhs instanceof long[]) {
        append((long[]) lhs, (long[]) rhs);
    } else if (lhs instanceof int[]) {
        append((int[]) lhs, (int[]) rhs);
    } else if (lhs instanceof short[]) {
        append((short[]) lhs, (short[]) rhs);
    } else if (lhs instanceof char[]) {
        append((char[]) lhs, (char[]) rhs);
    } else if (lhs instanceof byte[]) {
        append((byte[]) lhs, (byte[]) rhs);
    } else if (lhs instanceof double[]) {
        append((double[]) lhs, (double[]) rhs);
    } else if (lhs instanceof float[]) {
        append((float[]) lhs, (float[]) rhs);
    } else if (lhs instanceof boolean[]) {
        append((boolean[]) lhs, (boolean[]) rhs);
    } else {
        // Not an array of primitives
        append((Object[]) lhs, (Object[]) rhs);
    }
    return this;
}

public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass,
            String[] excludeFields) {
    if (lhs == rhs) {
        return true;
    }
    if (lhs == null || rhs == null) {
        return false;
    }
    // Find the leaf class since there may be transients in the leaf 
    // class or in classes between the leaf and root.
    // If we are not testing transients or a subclass has no ivars, 
    // then a subclass can test equals to a superclass.
    Class lhsClass = lhs.getClass();
    Class rhsClass = rhs.getClass();
    Class testClass;
    if (lhsClass.isInstance(rhs)) {
        testClass = lhsClass;
        if (!rhsClass.isInstance(lhs)) {
            // rhsClass is a subclass of lhsClass
            testClass = rhsClass;
        }
    } else if (rhsClass.isInstance(lhs)) {
        testClass = rhsClass;
        if (!lhsClass.isInstance(rhs)) {
            // lhsClass is a subclass of rhsClass
            testClass = lhsClass;
        }
    } else {
        // The two classes are not related.
        return false;
    }
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    try {
        equalsBuilder.reflectionAppend(lhs, rhs, testClass, testTransients, excludeFields);
        while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
            testClass = testClass.getSuperclass();
            equalsBuilder.reflectionAppend(lhs, rhs, testClass, testTransients, excludeFields);
        }
    } catch (IllegalArgumentException e) {
        // In this case, we tried to test a subclass vs. a superclass and
        // the subclass has ivars or the ivars are transient and 
        // we are testing transients.
        // If a subclass has ivars that we are trying to test them, we get an
        // exception and we know that the objects are not equal.
        return false;
    }
    return equalsBuilder.isEquals();
}