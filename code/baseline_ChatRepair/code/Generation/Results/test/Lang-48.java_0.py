import java.math.BigDecimal;

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

    // Specifically handle BigDecimal
    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        isEquals = ((BigDecimal) lhs).compareTo((BigDecimal) rhs) == 0;
        return this;
    }

    Class lhsClass = lhs.getClass();
    if (!lhsClass.isArray()) {
        // The simple case, not an array, just test the element
        isEquals = lhs.equals(rhs);
    } else if (lhs.getClass() != rhs.getClass()) {
        // Different dimensions or types for arrays
        this.setEquals(false);
    } else {
        // Array handling
        handleArrayComparison(lhs, rhs);
    }
    return this;
}

private void handleArrayComparison(Object lhs, Object rhs) {
    // 'Switch' on type of array, to dispatch to the correct handler
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
        // Not an array of primitives
        append((Object[]) lhs, (Object[]) rhs);
    }
}
