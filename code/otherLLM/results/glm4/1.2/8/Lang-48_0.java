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
    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        // Convert both to the same scale and compare them as Strings to handle different representations
        String lhsStr = ((BigDecimal) lhs).setScale(0, RoundingMode.HALF_EVEN).toPlainString();
        String rhsStr = ((BigDecimal) rhs).setScale(0, RoundingMode.HALF_EVEN).toPlainString();
        isEquals = lhsStr.equals(rhsStr);
    } else if (lhs instanceof BigDecimal || rhs instanceof BigDecimal) {
        // If only one of them is a BigDecimal, they are not equal
        this.setEquals(false);
    } else {
        Class<?> lhsClass = lhs.getClass();
        Class<?> rhsClass = rhs.getClass();

        // Use reflection to compare the two objects
        if (!lhsClass.isInstance(rhs)) {
            this.setEquals(false);
        } else if (reflectionEquals(lhs, rhs, false, null, null)) {
            isEquals = true;
        }
    }
    return this;
}