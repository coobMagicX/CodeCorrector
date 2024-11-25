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

    // Use reflectionEquals to check the equality of BigDecimal objects
    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        isEquals = lhs.equals(rhs);
    } else {
        Class<?> lhsClass = lhs.getClass();
        Class<?> rhsClass = rhs.getClass();

        if (!lhsClass.isInstance(rhs)) {
            this.setEquals(false);
        } else if (!rhsClass.isInstance(lhs)) {
            this.setEquals(false);
        } else {
            // For non-BigDecimal objects, use reflectionEquals for comparison
            isEquals = reflectionEquals(lhs, rhs, false, Object.class, null);
        }
    }

    return this;
}

// The reflectionEquals method remains unchanged as it's already defined in the context.