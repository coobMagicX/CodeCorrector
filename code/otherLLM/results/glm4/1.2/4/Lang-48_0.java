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

    if (!lhs.getClass().equals(rhs.getClass())) {
        this.setEquals(false);
        return this;
    }

    if (lhs instanceof BigDecimal) {
        BigDecimal lhsBigDecimal = (BigDecimal) lhs;
        BigDecimal rhsBigDecimal = (BigDecimal) rhs;

        // Check for equality of BigDecimal values ignoring scale and precision.
        isEquals = lhsBigDecimal.compareTo(rhsBigDecimal) == 0;
        if (!isEquals) {
            this.setEquals(false);
        }
    } else if (lhs.getClass().isArray()) {
        if (lhs.getClass() != rhs.getClass()) {
            this.setEquals(false);
            return this;
        }

        int length = Array.getLength(lhs);
        if (length != Array.getLength(rhs)) {
            this.setEquals(false);
            return this;
        }

        for (int i = 0; i < length && isEquals; ++i) {
            append(Array.get(lhs, i), Array.get(rhs, i));
        }
    } else {
        // For non-array and non-BigDecimal objects, use the existing equals method
        isEquals = lhs.equals(rhs);
        if (!isEquals) {
            this.setEquals(false);
        }
    }

    return this;
}