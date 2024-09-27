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
    Class rhsClass = rhs.getClass();
    if (!lhsClass.isArray() || !rhsClass.isArray()) {
        // Use reflectionEquals to handle cases like BigDecimal where equals might not be straightforward
        isEquals = reflectionEquals(lhs, rhs, true, null, null);
    } else if (lhsClass != rhsClass) {
        // Different array types should be considered not equal
        this.setEquals(false);
    } else {
        // Both are arrays of the same type
        if (lhs instanceof long[]) {
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
            // Both are object arrays of the same type
            append((Object[]) lhs, (Object[]) rhs);
        }
    }
    return this;
}