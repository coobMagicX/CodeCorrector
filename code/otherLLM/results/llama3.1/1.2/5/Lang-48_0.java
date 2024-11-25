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
    else if (lhs instanceof Object[]) {
        append((Object[]) lhs, (Object[]) rhs);
    } else if (lhs instanceof long[] || lhs instanceof int[] || lhs instanceof short[]
            || lhs instanceof char[] || lhs instanceof byte[] || lhs instanceof double[]
            || lhs instanceof float[] || lhs instanceof boolean[]) {
        Class clazz = lhs.getClass();
        if (clazz.getComponentType() != rhs.getClass().getComponentType()) {
            this.setEquals(false);
        } else {
            append((Object[]) lhs, (Object[]) rhs);
        }
    } else {
        // Not an array of primitives
        append((Object[]) lhs, (Object[]) rhs);
    }
    return this;
}