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

    Class<?> lhsClass = lhs.getClass();
    Class<?> rhsClass = rhs.getClass();

    if (!lhsClass.equals(rhsClass)) {
        this.setEquals(false);
        return this;
    }

    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        BigDecimal lhsBigDecimal = (BigDecimal) lhs;
        BigDecimal rhsBigDecimal = (BigDecimal) rhs;
        isEquals = lhsBigDecimal.compareTo(rhsBigDecimal) == 0;
    } else {
        // Fallback for other types
        super.append(lhs, rhs);
    }
    return this;
}