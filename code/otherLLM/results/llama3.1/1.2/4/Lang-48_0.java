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
    } else {
        // Add handling for BigDecimal instances
        if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
            appendBigDecimal((BigDecimal) lhs, (BigDecimal) rhs);
        } 
        // Generalize array comparison to handle arrays of primitive types in a more generic way
        else if (lhs instanceof long[] || lhs instanceof int[] || lhs instanceof short[] || lhs instanceof char[]
                || lhs instanceof byte[] || lhs instanceof double[] || lhs instanceof float[] || lhs instanceof boolean[]) {
            appendPrimitiveArray((long[]) lhs, (long[]) rhs);
        } 
        // Not an array of primitives or BigDecimal
        else {
            this.setEquals(false);
        }
    }
    return this;
}

private void appendBigDecimal(BigDecimal lhs, BigDecimal rhs) {
    if (!lhs.scale() == rhs.scale()) {
        this.setEquals(false);
    }
    int maxPrecision = Math.max(lhs.precision(), rhs.precision());
    for (int i = 0; i <= maxPrecision; i++) {
        long lhsIntPart = lhs.movePointRight(i).longValue();
        long rhsIntPart = rhs.movePointRight(i).longValue();
        if (lhsIntPart != rhsIntPart) {
            this.setEquals(false);
            return;
        }
    }
}

private void appendPrimitiveArray(long[] lhs, long[] rhs) {
    if (lhs.length != rhs.length) {
        this.setEquals(false);
        return;
    }
    for (int i = 0; i < lhs.length; i++) {
        if (lhs[i] != rhs[i]) {
            this.setEquals(false);
            return;
        }
    }
}