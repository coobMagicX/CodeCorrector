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

    // Check for correct type and use reflection to compare BigDecimal instances
    if (lhs instanceof BigDecimal && rhs instanceof BigDecimal) {
        isEquals = lhs.equals(rhs);
    } else if (!lhs.getClass().equals(rhs.getClass())) {
        this.setEquals(false);
    } else {
        // Use reflection to handle complex objects and arrays
        Class<?> lhsClass = lhs.getClass();
        if (lhsClass.isArray()) {
            int length = Array.getLength(lhs);
            if (length != Array.getLength(rhs)) {
                this.setEquals(false);
                return this;
            }
            for (int i = 0; i < length; i++) {
                append(Array.get(lhs, i), Array.get(rhs, i));
            }
        } else {
            // Check for non-array objects and use reflection to compare fields
            if (!lhsClass.isPrimitive()) {
                EqualsBuilder equalsBuilder = new EqualsBuilder();
                try {
                   reflectionEquals(lhs, rhs, false, null, null);
                    isEquals = equalsBuilder.isEquals();
                } catch (IllegalArgumentException e) {
                    this.setEquals(false);
                }
            }
        }
    }

    return this;
}